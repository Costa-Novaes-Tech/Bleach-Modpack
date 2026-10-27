from pathlib import Path
base=Path('C:/Users/joaop/Downloads/Projeto_Programacao_BleachMod')
p=base/'Bleach-Modpack-design/src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java'
s=p.read_text(encoding='utf-8')
s=s.replace('            "03_combo_back",\n            "04_hakuteiken_front",\n            "05_hakuteiken_profile",\n            "06_hakuteiken_back"','            "03_combo_back"')
s=s.replace('age >= 30 && age < 85','age >= 30 && age < 32')
s=s.replace('Husk h = EntityType.HUSK.create(p.serverLevel());','Husk h = null; // Proof141: no target can occlude the isolated diagonal.')
s=s.replace('if (CASES[t.index()].contains("profile")) {','if (t.index() < 3) {')
s=s.replace('Vec3 eye = focus.add(-4.5, 0.4, 0.5);','Vec3 eye = focus.add(t.index()==0 ? new Vec3(0,0.25,4.2) : t.index()==1 ? new Vec3(-4.2,0.25,0) : new Vec3(0,0.25,-4.2));')
s=s.replace('if (age < 25 || age > 120','if (age < 25 || age > 90')
s=s.replace('if (!captureThisTick) return;','captureThisTick = t.index() < 3 && age >= 25 && age <= 90;\n        if (!captureThisTick) return;')
needle='            Files.createDirectories(evidence);'
s=s.replace(needle,'''            Files.createDirectories(evidence);
            var poseData=new com.google.gson.JsonObject();
            poseData.addProperty("age",age);poseData.addProperty("partialTick",mc.getFrameTime());poseData.addProperty("worldTick",mc.level.getGameTime());
            poseData.addProperty("comboCount",((net.bettercombat.logic.PlayerAttackProperties)mc.player).getComboCount());
            if(mc.player instanceof dev.kosmx.playerAnim.impl.IAnimatedPlayer animated){
                var ap=animated.playerAnimator_getAnimation();poseData.addProperty("applierActive",ap.isActive());
                for(String bone:List.of("rightArm","rightItem","body")){
                    var rot=ap.get3DTransform(bone,dev.kosmx.playerAnim.api.TransformType.ROTATION,dev.kosmx.playerAnim.core.util.Vec3f.ZERO);
                    poseData.addProperty(bone,rot.getX()+","+rot.getY()+","+rot.getZ());
                }
            }
            Files.writeString(evidence.resolve("pose-telemetry.jsonl"),poseData+System.lineSeparator(),StandardOpenOption.CREATE,StandardOpenOption.APPEND);''')
p.write_text(s,encoding='utf-8')
b=base/'Bleach-Modpack-benihime';p=b/'src/main/java/com/bleachmod/client/cinematic/CinematicQaCommands.java';s=p.read_text(encoding='utf-8')
s=s.replace('var target=EntityType.HUSK.create(p.serverLevel());if(target==null)return;', '''net.minecraft.world.entity.Mob target=scenario.equals("threshold") ? com.bleachmod.registry.ModEntities.HOLLOW.get().create(p.serverLevel()) : EntityType.HUSK.create(p.serverLevel());if(target==null)return;
        if(target instanceof com.bleachmod.entity.HollowEntity hollow)hollow.setVariant(com.bleachmod.entity.HollowVariant.FLYING);''')
s=s.replace('scenario.equals("threshold")?100:20','20').replace('scenario.equals("threshold")?7:10','10')
p.write_text(s,encoding='utf-8')
p=b/'src/main/java/com/bleachmod/client/cinematic/CinematicQaSequence.java';s=p.read_text(encoding='utf-8')
s=s.replace('finish();scenario=null;stage=8;','finish();scenario=null;stage=8;\n            if(Boolean.getBoolean("bleachmod.qaAutoExit"))mc.stop();')
s=s.replace('age<56?"HOLD":age<72?"IGNITE":age<88?"ASH"','age<36?"HOLD":age<48?"IGNITE":age<68?"ASH"')
s=s.replace('metadata.addProperty("executionCamera",','metadata.addProperty("caseNote",scenario.equals("threshold")?"flying hollow normal lethal, not threshold":"normal lethal/control");metadata.addProperty("executionCamera",')
p.write_text(s,encoding='utf-8')
print('QA instrumentation only; no combat poses, damage or render rules changed')
