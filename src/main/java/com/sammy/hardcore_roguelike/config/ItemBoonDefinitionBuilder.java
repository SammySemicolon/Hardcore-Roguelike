package com.sammy.hardcore_roguelike.config;

import com.sammy.hardcore_roguelike.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.*;

import java.util.*;
import java.util.stream.*;

public class ItemBoonDefinitionBuilder {
    public String itemName;
    public int itemQuantity;
    public boolean boonActive;
    public int progressTillNextBoon;

    public ItemBoonDefinitionBuilder(String itemName, int itemQuantity, boolean boonActive, int progressTillNextBoon) {
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.boonActive = boonActive;
        this.progressTillNextBoon = progressTillNextBoon;
    }

    public ItemBoonDefinitionBuilder setBoonActive(boolean boonActive) {
        this.boonActive = boonActive;
        return this;
    }

    public ItemBoonDefinition build() {
        return new ItemBoonDefinition(itemName, itemQuantity, boonActive, progressTillNextBoon);
    }

    public MutableComponent createBoonText() {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        return Component.translatable(item.getDescriptionId()).append(Component.literal(" x" + itemQuantity));
    }

    public Component createProgressComponent() {
        return Component.translatable("hardcore_roguelike.editItemBoon.progress")
                .append(Component.literal(" "+progressTillNextBoon + "/" + Config.AMOUNT_OF_ITEMS_FOR_BOON.get()))
                .withStyle(ChatFormatting.GRAY);
    }

    public List<FormattedCharSequence> createBoonCheckboxTooltip() {
        List<FormattedCharSequence> charSequence = new ArrayList<>();
        charSequence.add(createBoonText().withStyle(ChatFormatting.YELLOW).getVisualOrderText());
        charSequence.add(createProgressComponent().getVisualOrderText());
        return charSequence;
    }

    public static Map<String, ItemBoonDefinitionBuilder> makeBoonCache() {
        Map<String, ItemBoonDefinitionBuilder> boonCache = new HashMap<>();
        for (Map.Entry<String, ItemBoonDefinition> entry : HardcoreRoguelikeMod.BOONS.entrySet()) {
            boonCache.put(entry.getKey(), entry.getValue().edit());
        }
        return boonCache;
    }

    public static void updateBoonsListUsingCache(Map<String, ItemBoonDefinitionBuilder> boonCache) {
        HardcoreRoguelikeMod.BOONS.clear();
        for (Map.Entry<String, ItemBoonDefinitionBuilder> entry : boonCache.entrySet()) {
            HardcoreRoguelikeMod.BOONS.put(entry.getKey(), entry.getValue().build());
        }
        Config.ITEM_BOONS.set(HardcoreRoguelikeMod.BOONS.values().stream().map(ItemBoonDefinition::toString).collect(Collectors.toList()));
    }
}
