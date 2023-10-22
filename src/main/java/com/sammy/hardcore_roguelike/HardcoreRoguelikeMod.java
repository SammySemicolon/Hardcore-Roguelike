package com.sammy.hardcore_roguelike;

import com.mojang.logging.LogUtils;
import com.sammy.hardcore_roguelike.capability.*;
import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;
import org.slf4j.Logger;

import java.util.*;

@Mod(HardcoreRoguelikeMod.MODID)
public class HardcoreRoguelikeMod {
    public static final String MODID = "hardcore_roguelike";
    public static final String BOON_SPAWNED_ITEM = "hardcore_roguelike:boon_spawned_item";

    public static final HashMap<String, ItemBoonDefinition> BOONS = new HashMap<>();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Block> BOUNTIFUL_ALTAR_BLOCK = BLOCKS.register("bountiful_altar", () -> new BountifulAltarBlock(BlockBehaviour.Properties.of().strength(5.0F, 1200.0F).mapColor(MapColor.DIAMOND)));
    public static final RegistryObject<Item> BOUNTIFUL_ALTAR_ITEM = ITEMS.register("bountiful_altar", () -> new BlockItem(BOUNTIFUL_ALTAR_BLOCK.get(), new Item.Properties()));

    public HardcoreRoguelikeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(PlayerDataCapability::registerCapabilities);
        modEventBus.addListener(HardcoreRoguelikeMod::creativeTabSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public static ResourceLocation path(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void creativeTabSetup(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.FUNCTIONAL_BLOCKS)) {
            event.accept(BOUNTIFUL_ALTAR_ITEM);
        }
    }
}
