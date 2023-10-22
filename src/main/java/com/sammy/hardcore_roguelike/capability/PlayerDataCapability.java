package com.sammy.hardcore_roguelike.capability;

import com.sammy.hardcore_roguelike.*;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.player.*;

public class PlayerDataCapability implements INBTSerializable<CompoundTag> {

    public static Capability<PlayerDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public boolean claimedBoons;

    public PlayerDataCapability() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerDataCapability.class);
    }

    public static void attachPlayerCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            final PlayerDataCapability capability = new PlayerDataCapability();
            event.addCapability(HardcoreRoguelikeMod.path("player_data"), new LodestoneCapabilityProvider<>(PlayerDataCapability.CAPABILITY, () -> capability));
        }
    }

    public static void playerClone(PlayerEvent.Clone event) {
        PlayerDataCapability originalCapability = PlayerDataCapability.getCapabilityOptional(event.getOriginal()).orElse(new PlayerDataCapability());
        PlayerDataCapability capability = PlayerDataCapability.getCapabilityOptional(event.getEntity()).orElse(new PlayerDataCapability());
        capability.deserializeNBT(originalCapability.serializeNBT());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("claimedBoons", claimedBoons);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        claimedBoons = tag.getBoolean("claimedBoons");
    }

    public static LazyOptional<PlayerDataCapability> getCapabilityOptional(Player player) {
        return player.getCapability(CAPABILITY);
    }

    public static PlayerDataCapability getCapability(Player player) {
        return player.getCapability(CAPABILITY).orElse(new PlayerDataCapability());
    }
}