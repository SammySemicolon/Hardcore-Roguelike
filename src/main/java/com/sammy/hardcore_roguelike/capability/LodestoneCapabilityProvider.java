package com.sammy.hardcore_roguelike.capability;

import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.*;

import javax.annotation.*;

public class LodestoneCapabilityProvider<C extends INBTSerializable<CompoundTag>> implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private final C instance;
    private final LazyOptional<C> capOptional;

    private final Capability<C> capability;

    public LodestoneCapabilityProvider(Capability<C> capability, NonNullSupplier<C> capInstance) {
        this.capability = capability;
        this.instance = capInstance.get();
        this.capOptional = LazyOptional.of(capInstance);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return capability.orEmpty(cap, capOptional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }
}