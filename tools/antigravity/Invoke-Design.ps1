param(
    [ValidateSet('Check', 'GameTests', 'Client', 'ClientQa')][string]$Mode = 'Check',
    [switch]$DryRun
)
$ErrorActionPreference = 'Stop'
$designRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '../..'))
$expectedRoot = 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack-design'
if ($designRoot -ne $expectedRoot) { throw "Unexpected design checkout: $designRoot" }
$branch = (& git -C $designRoot branch --show-current).Trim()
if ($LASTEXITCODE -ne 0 -or ($branch -ne 'codex/design-poderes-criaturas' -and $branch -ne 'codex/uryu-arqueiro-tradicional')) { throw "Wrong design branch: $branch" }
$jdk = Get-ChildItem -Path 'C:/Program Files/Eclipse Adoptium/jdk-17*' -Directory | Where-Object { Test-Path (Join-Path $_.FullName 'bin/java.exe') } | Select-Object -First 1
if (!$jdk) { throw 'Existing Java17 not found; do not update Java blindly.' }
$gradleTasks = switch ($Mode) { 'Check' { @('check','build') }; 'GameTests' { @('runGameTestServer') }; 'Client' { @('runClient') }; 'ClientQa' { @('-PbenihimeQa=true','-PhyorinmaruQa=true','-PsenbonzakuraQa=true','-PqaAutoExit=true','runClient') } }
Write-Host "Root=$designRoot Branch=$branch Java=$($jdk.FullName) Tasks=$gradleTasks"
if ($DryRun) { return }
$lock = [Threading.Mutex]::new($false, 'Local\BleachMod-Design-Gradle')
if (!$lock.WaitOne(0)) { $lock.Dispose(); throw 'Another design Gradle task is active.' }
$originalJava = $env:JAVA_HOME
$originalPath = $env:Path
try {
    $activeGame = Get-CimInstance Win32_Process -Filter "name = 'java.exe' OR name = 'javaw.exe'" | Where-Object { $_.CommandLine -match 'BootstrapLauncher|forgeclient|GradleWrapperMain.*(runClient|runGameTestServer)' }
    if ($activeGame) { throw 'A Minecraft/test client is already active. Coordinate editor/game ownership first.' }
    $env:JAVA_HOME = $jdk.FullName
    $env:Path = "$($jdk.FullName)\bin;$originalPath"
    Push-Location $designRoot
    try {
        $logs = Join-Path $designRoot 'Docs/poderes/qa/antigravity'
        New-Item -ItemType Directory -Path $logs -Force | Out-Null
        $stamp = Get-Date -Format 'yyyyMMdd-HHmmss'
        $log = Join-Path $logs "$Mode-$stamp.log"
        if ($Mode -eq 'ClientQa') {
            $qaOptions = Join-Path $designRoot 'run-benihime-qa/base/options.txt'
            if (!(Test-Path -LiteralPath $qaOptions)) {
                New-Item -ItemType Directory -Path (Split-Path -Parent $qaOptions) -Force | Out-Null
                # Only a new disposable QA profile; never touch personal game options.
                [IO.File]::WriteAllLines($qaOptions, @('onboardAccessibility:false', 'pauseOnLostFocus:false'))
            }
        }
        $nativeArgs = 'gradlew.bat --no-daemon --console=plain ' + ($gradleTasks -join ' ')
        $windowStyle = if ($Mode -eq 'Client') { 'Normal' } else { 'Hidden' }
        $process = Start-Process -FilePath $env:ComSpec -ArgumentList @('/d', '/c', $nativeArgs) -WorkingDirectory $designRoot -WindowStyle $windowStyle -RedirectStandardOutput $log -RedirectStandardError "$log.stderr.log" -Wait -PassThru
        $code = $process.ExitCode
        Write-Host "Exit=$code Log=$log"
        Get-Content -LiteralPath $log -Tail 20
        if ($code -ne 0) { throw "Gradle failed ($code). Inspect $log; do not mark QA passed." }
    } finally { Pop-Location }
} finally {
    $env:JAVA_HOME = $originalJava
    $env:Path = $originalPath
    $lock.ReleaseMutex()
    $lock.Dispose()
}
