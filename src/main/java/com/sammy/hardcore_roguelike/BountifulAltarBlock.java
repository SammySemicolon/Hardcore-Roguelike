package com.sammy.hardcore_roguelike;

import com.sammy.hardcore_roguelike.config.*;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

import java.util.*;

import static com.sammy.hardcore_roguelike.HardcoreRoguelikeMod.*;

public class BountifulAltarBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public BountifulAltarBlock(Properties p_49795_) {
        super(p_49795_);
    }

    public VoxelShape getShape(BlockState p_52988_, BlockGetter p_52989_, BlockPos p_52990_, CollisionContext p_52991_) {
        return SHAPE;
    }

    public RenderShape getRenderShape(BlockState p_52986_) {
        return RenderShape.MODEL;
    }

    public boolean isPathfindable(BlockState p_52969_, BlockGetter p_52970_, BlockPos p_52971_, PathComputationType p_52972_) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getTag();
        if (!Config.ALLOW_UNSTACKABLE_BOONS.get()) {
            if (!stack.isStackable()) {
                return InteractionResult.PASS;
            }
        }
        else {
            if (stack.isDamaged()) {
                return InteractionResult.PASS;
            }
        }
        if (stack.isEmpty() || (tag != null && tag.contains(BOON_SPAWNED_ITEM))) {
            return InteractionResult.PASS;
        }
        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        Map<String, ItemBoonDefinitionBuilder> boonCache = ItemBoonDefinitionBuilder.makeBoonCache();
        boonCache.computeIfAbsent(itemId, k -> new ItemBoonDefinitionBuilder(k, 0, true, 0));
        ItemBoonDefinitionBuilder currentBuilder = boonCache.get(itemId);
        if (currentBuilder.itemQuantity > Config.MAXIMUM_QUANTITY_FOR_ITEM_BOON.get()) {
            return InteractionResult.PASS;
        }

        int quantityForBoon = Config.AMOUNT_OF_ITEMS_FOR_BOON.get();
        int count = stack.getCount();
        int leftOver = count % quantityForBoon;
        if (Config.ALLOW_PARTIAL_DEPOSITS.get()) {
            if (!level.isClientSide) {
                currentBuilder.itemQuantity += (count - leftOver) / quantityForBoon;
                currentBuilder.progressTillNextBoon += leftOver;
                if (currentBuilder.progressTillNextBoon >= 10) {
                    currentBuilder.progressTillNextBoon -= 10;
                    currentBuilder.itemQuantity++;
                }
                currentBuilder.boonActive = currentBuilder.itemQuantity > 0;
                player.setItemInHand(hand, ItemStack.EMPTY);
                ItemBoonDefinitionBuilder.updateBoonsListUsingCache(boonCache);
            }
            playNoise(level, pos);
            return InteractionResult.SUCCESS;
        }
        else if (count > quantityForBoon) {
            if (!level.isClientSide) {
                while (count > quantityForBoon) {
                    currentBuilder.itemQuantity++;
                    count -= quantityForBoon;
                }
                stack.setCount(leftOver);
                ItemBoonDefinitionBuilder.updateBoonsListUsingCache(boonCache);
            }
            playNoise(level, pos);
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    public void playNoise(Level level, BlockPos pos) {
        RandomSource randomSource = level.random;
        level.playSound(null, pos, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 0.5f, Mth.nextFloat(randomSource, 1.75f, 2f));
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1, Mth.nextFloat(randomSource, 0.75f, 1.25f));
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1, Mth.nextFloat(randomSource, 0.75f, 1.25f));
    }
}
