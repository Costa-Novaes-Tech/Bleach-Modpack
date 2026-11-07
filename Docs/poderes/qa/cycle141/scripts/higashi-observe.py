from pathlib import Path
b=Path('C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod/Bleach-Modpack-benihime')
p=b/'src/main/java/com/bleachmod/client/model/HigashiExecutionVisuals.java';s=p.read_text(encoding='utf-8')
s=s.replace('    private static boolean renderingSilhouette;','    private static boolean renderingSilhouette;\n    private static long proofSuppressedTick=Long.MIN_VALUE,proofSilhouetteTick=Long.MIN_VALUE;')
s=s.replace('return !renderingSilhouette && isExecuting(entity);','''boolean suppressed=!renderingSilhouette && isExecuting(entity);
        if(suppressed && Boolean.getBoolean("bleachmod.higashiPoseQa") && proofSuppressedTick!=now()){
            proofSuppressedTick=now();com.mojang.logging.LogUtils.getLogger().info("PROOF141 original render blocked entity={} tick={}",entity,now());
        }
        return suppressed;''')
s=s.replace('long elapsed=now-b.start;','''long elapsed=now-b.start;
            if(Boolean.getBoolean("bleachmod.higashiPoseQa") && proofSilhouetteTick!=now && elapsed%4==0){
                proofSilhouetteTick=now;com.mojang.logging.LogUtils.getLogger().info("PROOF141 silhouette target={} type={} elapsed={} variant={} originalPresent={}",b.packet.target(),actor.getType(),elapsed,actor instanceof com.bleachmod.entity.HollowEntity h?h.getVariant():"vanilla",mc.level.getEntity(b.packet.target())!=null);
            }''')
p.write_text(s,encoding='utf-8')
print('Higashi render observability instrumentation ready')
