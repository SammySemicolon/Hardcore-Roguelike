package com.sammy.hardcore_roguelike.config;

import com.sammy.hardcore_roguelike.*;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.*;

@Mod.EventBusSubscriber(modid = HardcoreRoguelikeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue WORKS_ON_HARDCORE_ONLY = BUILDER
            .comment("Should the mod's functionality only work on hardcore worlds?")
            .define("hardcoreOnly", true);

    public static final ForgeConfigSpec.BooleanValue ALLOW_ANY_DIFFICULTY_IN_HARDCORE = BUILDER
            .comment("Should hardcore mode be playable in any difficulty?")
            .define("allowAnyHardcoreDifficulty", true);

    public static final ForgeConfigSpec.BooleanValue ALLOW_PARTIAL_DEPOSITS = BUILDER
            .comment("Should the mod accept partial deposits into the altar that do not amount to an increase in quantity for your item boom?")
            .comment("If enabled, the player will be able to deposit a percentage of what's needed for the next quantity of item boon, then start a new world with that deposit saved.")
            .comment("If disabled, the bountiful altar will not accept any quantity of item that's below the exchange rate.")
            .define("partialDeposits", true);

    public static final ForgeConfigSpec.BooleanValue ALLOW_UNSTACKABLE_BOONS = BUILDER
            .comment("Should you be able to sacrifice an unstackable item? Damaged items will still be invalid for sacrifice.")
            .define("allowUnstackableSacrifices", false);

    public static final ForgeConfigSpec.IntValue AMOUNT_OF_ITEMS_FOR_BOON = BUILDER
            .comment("The item to item boon ratio at the bountiful altar")
            .defineInRange("itemBoonExchangeRate", 10, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.IntValue MAXIMUM_QUANTITY_FOR_ITEM_BOON = BUILDER
            .comment("The maximum amount of an item a single boon can grant.")
            .defineInRange("itemBoonMaximumQuantity", 512, 1, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_BOONS = BUILDER
            .comment("This is where your item boon options are stored.")
            .comment("You may edit this list however you want, to restore lost data or just give yourself an advantage.")
            .comment("Generally speaking, you should not edit this list while the game is open, changes you make might be overwritten.")
            .comment("Each item boon should be formatted as such:")
            .comment("\"item_registry_name,quantity,status,progress_till_next_boon\"")
            .comment(" - The item registry name defines which item the boon represents.")
            .comment(" - The quantity is the amount of the item you will receive, and will be incremented by the altar.")
            .comment(" - The status determines whether or not the boon is active.")
            .comment(" - The progress till next boon value is used to keep track how close we are until we are able to increment the quantity value.")
            .comment("Here's an example: \"minecraft:diamond, 400, true, 3\"")
            .defineListAllowEmpty("wawawa", List.of(),
                    Config::validateWhatever);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validateWhatever(final Object obj) {
        ItemBoonDefinition boonDefinition = null;
        if (obj instanceof String string) {
            try {
                boonDefinition = ItemBoonDefinition.fromString(string);
            }
            catch (RuntimeException runtimeException) {
                return false;
            }
        }
        HardcoreRoguelikeMod.BOONS.put(boonDefinition.itemName(), boonDefinition);
        return true;
    }
}