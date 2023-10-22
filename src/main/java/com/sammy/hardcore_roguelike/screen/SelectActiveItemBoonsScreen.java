package com.sammy.hardcore_roguelike.screen;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.*;
import javax.annotation.Nullable;

import com.sammy.hardcore_roguelike.*;
import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SelectActiveItemBoonsScreen extends Screen {

   public static final Component ITEM_BOONS_LABEL = Component.translatable("hardcore_roguelike.selectWorld.itemBoons");


   public final Map<String, ItemBoonDefinitionBuilder> boonCache;

   private final Runnable exitCallback;
   private final Runnable doneCallback;
   private BoonList boonList;

   public SelectActiveItemBoonsScreen(CreateWorldScreen createWorldScreen) {
      super(Component.translatable("hardcore_roguelike.itemBoons.title"));
      this.boonCache = ItemBoonDefinitionBuilder.makeBoonCache();
      this.exitCallback = () -> minecraft.setScreen(createWorldScreen);
      this.doneCallback = () -> {
         ItemBoonDefinitionBuilder.updateBoonsListUsingCache(boonCache);
         exitCallback.run();
      };
   }

   public static void openItemBoonsScreen(CreateWorldScreen createWorldScreen) {
      Minecraft minecraft = Minecraft.getInstance();
      minecraft.setScreen(new SelectActiveItemBoonsScreen(createWorldScreen));
   }


   protected void init() {
      this.boonList = new BoonList();
      this.addWidget(this.boonList);
      GridLayout.RowHelper gridlayout$rowhelper = (new GridLayout()).columnSpacing(10).createRowHelper(2);

      gridlayout$rowhelper.addChild(Button.builder(CommonComponents.GUI_DONE, (button) -> this.doneCallback.run()).build());

      gridlayout$rowhelper.addChild(Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.exitCallback.run()).build());

      gridlayout$rowhelper.getGrid().visitWidgets(this::addRenderableWidget);
      gridlayout$rowhelper.getGrid().setPosition(this.width / 2 - 155, this.height - 28);
      gridlayout$rowhelper.getGrid().arrangeElements();
   }

   public void render(GuiGraphics p_282252_, int p_281351_, int p_282537_, float p_281589_) {
      this.boonList.render(p_282252_, p_281351_, p_282537_, p_281589_);
      p_282252_.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);
      super.render(p_282252_, p_281351_, p_282537_, p_281589_);
   }

   @OnlyIn(Dist.CLIENT)
   public class BooleanRuleEntry extends SelectActiveItemBoonsScreen.AbstractEntry {
      private final CycleButton<Boolean> checkbox;

      public BooleanRuleEntry(ItemBoonDefinitionBuilder boonDefinition) {
         super(boonDefinition.createBoonText(), boonDefinition.createBoonCheckboxTooltip());
         this.checkbox = CycleButton.onOffBuilder(boonDefinition.boonActive).displayOnlyValue()
                 .withCustomNarration(b -> b.createDefaultNarrationMessage().append("\n").append(boonDefinition.createProgressComponent().getString()))
                 .create(10, 5, 44, 20, Component.literal("I don't know where this is used even"), (button, value) -> boonDefinition.setBoonActive(value));
         this.children.add(this.checkbox);
      }

      public void render(GuiGraphics p_281587_, int p_281471_, int p_281257_, int p_282541_, int p_282993_, int p_283543_, int p_281322_, int p_282930_, boolean p_283227_, float p_283364_) {
         this.renderLabel(p_281587_, p_281257_, p_282541_);
         this.checkbox.setX(p_282541_ + p_282993_ - 45);
         this.checkbox.setY(p_281257_);
         this.checkbox.render(p_281587_, p_281322_, p_282930_, p_283364_);
      }

      public List<? extends GuiEventListener> children() {
         return this.children;
      }

      public List<? extends NarratableEntry> narratables() {
         return this.children;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public abstract class AbstractEntry extends ContainerObjectSelectionList.Entry<AbstractEntry> {
      @Nullable
      private final List<FormattedCharSequence> label;
      private final List<FormattedCharSequence> tooltip;
      protected final List<AbstractWidget> children = Lists.newArrayList();

      public AbstractEntry(Component label, List<FormattedCharSequence> tooltip) {
         this.label = minecraft.font.split(label, 175);
         this.tooltip = tooltip;
      }

      protected void renderLabel(GuiGraphics p_282711_, int p_281539_, int p_281414_) {
         if (this.label.size() == 1) {
            p_282711_.drawString(minecraft.font, this.label.get(0), p_281414_, p_281539_ + 5, 16777215, false);
         } else if (this.label.size() >= 2) {
            p_282711_.drawString(minecraft.font, this.label.get(0), p_281414_, p_281539_, 16777215, false);
            p_282711_.drawString(minecraft.font, this.label.get(1), p_281414_, p_281539_ + 10, 16777215, false);
         }
      }

   }

   @OnlyIn(Dist.CLIENT)
   public class BoonList extends ContainerObjectSelectionList<AbstractEntry> {
      public BoonList() {
         super(SelectActiveItemBoonsScreen.this.minecraft, SelectActiveItemBoonsScreen.this.width, SelectActiveItemBoonsScreen.this.height, 43, SelectActiveItemBoonsScreen.this.height - 32, 24);

         for (ItemBoonDefinitionBuilder itemBoonDefinitionBuilder : boonCache.values()) {
            addEntry(new BooleanRuleEntry(itemBoonDefinitionBuilder));
         }
      }

      @Override
      public void render(GuiGraphics p_282708_, int p_283242_, int p_282891_, float p_283683_) {
         super.render(p_282708_, p_283242_, p_282891_, p_283683_);
         AbstractEntry hovered = getHovered();
         if (hovered != null && hovered.tooltip != null) {
            setTooltipForNextRenderPass(hovered.tooltip);
         }
      }
   }
}