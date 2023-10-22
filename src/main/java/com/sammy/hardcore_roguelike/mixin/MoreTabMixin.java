package com.sammy.hardcore_roguelike.mixin;

import com.sammy.hardcore_roguelike.screen.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.worldselection.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import static com.sammy.hardcore_roguelike.screen.SelectActiveItemBoonsScreen.ITEM_BOONS_LABEL;

@Mixin(CreateWorldScreen.MoreTab.class)
public class MoreTabMixin {

    @Unique
    private GridLayout.RowHelper gridlayout$rowhelper;

    @Inject(method = "<init>", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void hardcoreRoguelike$gridLayoutRowHelperGrabberMixin(CreateWorldScreen p_268071_, CallbackInfo ci, GridLayout.RowHelper gridlayout$rowhelper) {
        this.gridlayout$rowhelper = gridlayout$rowhelper;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void hardcoreRoguelike$addItemBoonsButtonMixin(CreateWorldScreen p_268071_, CallbackInfo ci) {
        gridlayout$rowhelper.addChild(Button.builder(ITEM_BOONS_LABEL, (button) -> SelectActiveItemBoonsScreen.openItemBoonsScreen(p_268071_)).width(210).build());
    }
}
