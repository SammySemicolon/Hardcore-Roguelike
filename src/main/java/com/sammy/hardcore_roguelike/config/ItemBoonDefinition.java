package com.sammy.hardcore_roguelike.config;

import com.sammy.hardcore_roguelike.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.*;

public record ItemBoonDefinition(String itemName, int itemQuantity, boolean boonActive, int progressTillNextBoon) {

    public static ItemBoonDefinition fromString(String boon) {
        final String[] split = boon.replaceAll("\\s","").split(",");
        if (split.length != 4) {
            throw new RuntimeException("An item boon value is invalid, this is most likely a syntax error.");
        }
        String itemName = split[0];
        if (!ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName))) {
            throw new RuntimeException("An item boon value is invalid, the item defined doesn't exist.");
        }
        int itemQuantity = Integer.parseInt(split[1]);
        boolean boonActive = Boolean.parseBoolean(split[2]);
        int progressTillNextBoon = Integer.parseInt(split[3]);
        return new ItemBoonDefinition(itemName, itemQuantity, boonActive, progressTillNextBoon);
    }

    public ItemBoonDefinitionBuilder edit() {
        return new ItemBoonDefinitionBuilder(itemName, itemQuantity, boonActive, progressTillNextBoon);
    }

    @Override
    public String toString() {
        return itemName + "," + itemQuantity + "," + boonActive + "," + progressTillNextBoon;
    }

    public ItemStack createStack() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName()));
        final ItemStack stack = new ItemStack(item, this.itemQuantity());
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(HardcoreRoguelikeMod.BOON_SPAWNED_ITEM, true);
        stack.setTag(tag);
        return stack;
    }
}
