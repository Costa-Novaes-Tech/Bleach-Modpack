path = 'src/main/java/com/bleachmod/client/cinematic/SenbonzakuraHakuteikenQaSequence.java'
with open(path, 'r', encoding='utf-8') as f:
    text = f.read()

nl = '\r\n' if '\r\n' in text else '\n'

target1 = f"long age = mc.level.getGameTime() - trial.start();{nl}        if (age >= 52 && serverEvaluated) {{"
replace1 = f"long age = mc.level.getGameTime() - trial.start();{nl}        if (age >= 56 && serverEvaluated) {{"
assert target1 in text, 'target1 not found'
text = text.replace(target1, replace1)

target2 = "if (age >= 46 && serverSlotExecuted && !serverEvaluated) {"
replace2 = f"if (age == 49) {{{nl}            p.swing(InteractionHand.MAIN_HAND, true);{nl}        }}{nl}        if (age >= 46 && serverSlotExecuted && !serverEvaluated) {{"
assert target2 in text, 'target2 not found'
text = text.replace(target2, replace2)

target3 = "if (age == 6 || age == 12 || age == 20 || age == 28 || age == 34 || age == 40 || age == 46) {"
replace3 = f"if (age == 49) {{{nl}            mc.player.swing(InteractionHand.MAIN_HAND);{nl}        }}{nl}        if (age == 6 || age == 12 || age == 20 || age == 28 || age == 34 || age == 40 || age == 46 || age == 50) {{"
assert target3 in text, 'target3 not found'
text = text.replace(target3, replace3)

target4 = 'String phase = age < 10 ? "leap" : (age < 30 ? "hover" : (age < 40 ? "rasante" : (age == 40 ? "impact" : "transform")));'
replace4 = 'String phase = age < 10 ? "leap" : (age < 30 ? "hover" : (age < 40 ? "rasante" : (age == 40 ? "impact" : (age == 46 ? "transform" : "attack"))));'
assert target4 in text, 'target4 not found'
text = text.replace(target4, replace4)

with open(path, 'w', encoding='utf-8') as f:
    f.write(text)

print('Successfully applied QA attack capture updates.')
