package com.sammy.hardcore_roguelike.mixin;

import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.layouts.*;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(CreateWorldScreen.GameTab.class)
public class GameTabMixin {

    @Unique
    private static CycleButton<Difficulty> hardcoreRoguelike$cycleButton;

    @Inject(method = "<init>", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void hardcoreRoguelike$DifficultyCycleButtonGrabber(CreateWorldScreen p_268170_, CallbackInfo ci, GridLayout.RowHelper gridlayout$rowhelper, LayoutSettings layoutsettings, GridLayout.RowHelper gridlayout$rowhelper1, CycleButton cyclebutton, CycleButton cyclebutton1, CycleButton cyclebutton2) {
        GameTabMixin.hardcoreRoguelike$cycleButton = cyclebutton1;
    }

    @Inject(method = "lambda$new$6", at = @At(value = "TAIL"), require = 0)
    private static void hardcoreRoguelike$ActivateDifficultyButtonDev(Boolean value, CallbackInfoReturnable<Tooltip> cir) {
        if (hardcoreRoguelike$cycleButton != null && Config.ALLOW_ANY_DIFFICULTY_IN_HARDCORE.get()) {
            hardcoreRoguelike$cycleButton.active = true;
        }
    }

    @Inject(method = "m_267596_", at = @At(value = "TAIL"), require = 0)
    private static void hardcoreRoguelike$ActivateDifficultyButtonProd(Boolean value, CallbackInfoReturnable<Tooltip> cir) {
        if (hardcoreRoguelike$cycleButton != null && Config.ALLOW_ANY_DIFFICULTY_IN_HARDCORE.get()) {
            hardcoreRoguelike$cycleButton.active = true;
        }
    }
}
