package com.sammy.hardcore_roguelike;

import com.sammy.hardcore_roguelike.capability.*;
import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.registries.*;

import java.util.*;

@Mod.EventBusSubscriber(modid = HardcoreRoguelikeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        PlayerDataCapability.attachPlayerCapability(event);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerDataCapability.playerClone(event);
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            final PlayerDataCapability capability = PlayerDataCapability.getCapability(player);
            if (capability.claimedBoons || (Config.WORKS_ON_HARDCORE_ONLY.get() && !player.serverLevel().getLevelData().isHardcore())) {
                return;
            }
            for (ItemBoonDefinition boon : HardcoreRoguelikeMod.BOONS.values()) {
                if (!boon.boonActive() || boon.itemQuantity() == 0) {
                    continue;
                }
                player.spawnAtLocation(boon.createStack());
            }
            capability.claimedBoons = true;
        }
    }
}