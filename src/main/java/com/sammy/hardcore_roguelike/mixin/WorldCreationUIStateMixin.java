package com.sammy.hardcore_roguelike.mixin;

import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.client.gui.screens.worldselection.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(WorldCreationUiState.class)
public class WorldCreationUIStateMixin {

    @Shadow private Difficulty difficulty;

    @Inject(method = "getDifficulty", at = @At("HEAD"), cancellable = true)
    private void hardcoreRoguelike$getDifficulty(CallbackInfoReturnable<Difficulty> cir) {
        if (Config.ALLOW_ANY_DIFFICULTY_IN_HARDCORE.get()) {
            cir.setReturnValue(difficulty);
        }
    }
}
