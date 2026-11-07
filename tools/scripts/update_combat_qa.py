path = 'src/main/java/com/bleachmod/client/cinematic/SenbonzakuraCombatQaSequence.java'
with open(path, 'r', encoding='utf-8') as f:
    text = f.read()

nl = '\r\n' if '\r\n' in text else '\n'

target1 = '''    private static final String[] CASES = Boolean.getBoolean("bleachmod.senbonComboQa") ? new String[]{
            "01_shikai_right_profile", "02_shikai_right_front", "03_shikai_right_back",
            "04_sealed_right_profile", "05_bankai_right_profile", "06_shikai_left_profile"
    } : new String[]{
            "01_shikai_right_front",
            "02_shikai_right_profile",
            "03_shikai_right_back",
            "04_sealed_right_profile",
            "05_bankai_right_profile",
            "06_shikai_left_profile"
    };'''

replace1 = '''    private static final String[] CASES = Boolean.getBoolean("bleachmod.senbonComboQa") ? new String[]{
            "01_shikai_right_profile", "02_shikai_right_front", "03_shikai_right_back",
            "04_sealed_right_profile", "05_bankai_right_profile", "06_shikai_left_profile",
            "07_hakuteiken_combo_front"
    } : new String[]{
            "01_shikai_right_front",
            "02_shikai_right_profile",
            "03_shikai_right_back",
            "04_sealed_right_profile",
            "05_bankai_right_profile",
            "06_shikai_left_profile",
            "07_hakuteiken_combo_front"
    };'''

if nl == '\r\n':
    target1 = target1.replace('\n', '\r\n')
    replace1 = replace1.replace('\n', '\r\n')

assert target1 in text, 'target1 not found'
text = text.replace(target1, replace1)

target2 = '''        String caseName = CASES[index];
        String form = "shikai";
        if (caseName.contains("sealed")) {
            form = "sealed";
        } else if (caseName.contains("bankai")) {
            form = "bankai";
        }
        d.getCharacter().setActiveForm("zanpakuto", form);
        d.getCharacter().setSelectedForm("zanpakuto", form);'''

replace2 = '''        String caseName = CASES[index];
        String form = "shikai";
        if (caseName.contains("sealed")) {
            form = "sealed";
        } else if (caseName.contains("bankai") || caseName.contains("hakuteiken")) {
            form = "bankai";
        }
        d.getCharacter().setActiveForm("zanpakuto", form);
        d.getCharacter().setSelectedForm("zanpakuto", form);
        if (caseName.contains("hakuteiken")) {
            d.getCharacter().setSenbonBlade(2);
        } else {
            d.getCharacter().setSenbonBlade(0);
        }'''

if nl == '\r\n':
    target2 = target2.replace('\n', '\r\n')
    replace2 = replace2.replace('\n', '\r\n')

assert target2 in text, 'target2 not found'
text = text.replace(target2, replace2)

with open(path, 'w', encoding='utf-8') as f:
    f.write(text)

print('Updated SenbonzakuraCombatQaSequence with 07_hakuteiken_combo_front')
