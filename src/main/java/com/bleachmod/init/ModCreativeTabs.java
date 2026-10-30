package com.bleachmod.init;

import com.bleachmod.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .title(Component.translatable("itemGroup.bleachmod"))
            .icon(() -> ModItems.ASAUCHI.get().getDefaultInstance())
            .displayItems((params, output) -> {
                output.accept(ModItems.STORY_JOURNAL.get());
                output.accept(ModItems.ASAUCHI.get());
                output.accept(ModItems.RYUJIN_JAKKA.get());
                output.accept(ModItems.HYORINMARU.get());
                output.accept(ModItems.SENBONZAKURA.get()); output.accept(ModItems.BENIHIME.get());
                output.accept(ModItems.NOZARASHI.get());
                output.accept(ModItems.TRAVEL_HUMAN_WORLD.get());
                output.accept(ModItems.TRAVEL_SOUL_SOCIETY.get());
                output.accept(ModItems.TRAVEL_HUECO_MUNDO.get());
                output.accept(ModItems.HOLLOW_SPAWN_EGG.get());
                ModItems.npcSpawnEggs().values().forEach(egg -> output.accept(egg.get()));
            })
            .build());

    private ModCreativeTabs() {
    }
}
