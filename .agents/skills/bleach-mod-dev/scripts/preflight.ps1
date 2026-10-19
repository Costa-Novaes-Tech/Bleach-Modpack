# Bleach Mod preflight: leitura de ambiente, sem instalação e sem executar Gradle.
param(
    [string]$ProjectRoot = 'C:\Users\joaop\Downloads\Projeto_Programacao_BleachMod\Bleach-Modpack'
)
$ErrorActionPreference = 'Continue'
Write-Output "=== Bleach Mod | Preflight (somente leitura) ==="
Write-Output "Root: $ProjectRoot"
if (-not (Test-Path -LiteralPath $ProjectRoot -PathType Container)) {
    Write-Error "Projeto nao encontrado. Informe -ProjectRoot com o caminho correto."
    exit 1
}
Push-Location -LiteralPath $ProjectRoot
try {
    foreach ($name in @('git','java','javac','node','npm','python','ffmpeg','ffprobe')) {
        $cmd = Get-Command $name -ErrorAction SilentlyContinue
        if ($cmd) { Write-Output ("{0}: {1}" -f $name,$cmd.Source) }
        else { Write-Output ("{0}: NAO ENCONTRADO NO PATH" -f $name) }
    }
    Write-Output '--- Git ---'
    if (Get-Command git -ErrorAction SilentlyContinue) {
        & git branch --show-current
        & git status --short
        & git rev-parse --short HEAD
    }
    Write-Output '--- Arquivos base ---'
    foreach ($name in @('build.gradle','settings.gradle','gradle.properties','gradlew.bat','gradle/wrapper/gradle-wrapper.jar','AGENTS.md')) {
        Write-Output ("{0}: {1}" -f $name,(Test-Path -LiteralPath $name))
    }
    Write-Output '--- Java ---'
    if (Get-Command java -ErrorAction SilentlyContinue) { & java -version 2>&1 }
    Write-Output '--- JAVA_HOME ---'
    Write-Output $env:JAVA_HOME
    Write-Output 'Sem build, downloads ou instalacoes.'
} finally {
    Pop-Location
}
