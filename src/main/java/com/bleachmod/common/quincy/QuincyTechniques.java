package com.bleachmod.common.quincy;

import java.util.List;

/** Prototype metadata, separate from zanpakuto forms and their mastery archives. */
public final class QuincyTechniques {
    public record Definition(int slot, String id, String nameKey, float cost, int cooldown, float stamina) { }
    public static final List<Definition> ALL=List.of(
            new Definition(1,"seele_schneider","ability.bleachmod.quincy.seele",QuincyTechniqueService.SEELE_PE_COST,QuincyTechniqueService.SEELE_COOLDOWN_TICKS,0),
            new Definition(2,"licht_regen","ability.bleachmod.quincy.licht",QuincyTechniqueService.LICHT_REGEN_PE_COST,QuincyTechniqueService.LICHT_REGEN_COOLDOWN_TICKS,0),
            new Definition(3,"hirenkyaku","ability.bleachmod.quincy.hirenkyaku",HirenkyakuService.PE_COST,HirenkyakuService.COOLDOWN_TICKS,HirenkyakuService.STAMINA_COST),
            new Definition(4,"sprenger","ability.bleachmod.quincy.sprenger",QuincyTechniqueService.SPRENGER_PE_COST,QuincyTechniqueService.SPRENGER_COOLDOWN_TICKS,0));
    public static final List<Definition> ADVANCED=List.of(
        new Definition(1,"reishi_piercer","ability.bleachmod.quincy.piercer",16,120,0),
        new Definition(2,"reishi_arsenal","ability.bleachmod.quincy.arsenal",25,240,0),
        new Definition(3,"hirenkyaku_advanced","ability.bleachmod.quincy.step_advanced",8,120,10),
        new Definition(4,"reishi_convergence","ability.bleachmod.quincy.convergence",40,400,0));
    private QuincyTechniques() { }
    public static Definition slot(int slot) {
        if(slot<1||slot>4)throw new IllegalArgumentException("Invalid Quincy slot");
        return ALL.get(slot-1);
    }
}
