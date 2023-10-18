package com.alaharranhonor.vfl.item;

import com.alaharranhonor.vfl.ModRef;
import com.alaharranhonor.vfl.client.ClientModEventHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlashlightItem extends Item implements IFlashlight {

    private final boolean isUltra;
    public FlashlightItem(Properties pProperties, boolean isUltra) {
        super(pProperties);
        this.isUltra = isUltra;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack held = pPlayer.getItemInHand(pUsedHand);
        this.toggle(pLevel, held);
        return InteractionResultHolder.sidedSuccess(held, pLevel.isClientSide);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel.isClientSide()) {
            if (pIsSelected) {
                Minecraft.getInstance().gameRenderer.loadEffect(ModRef.res("shaders/post/flash.json"));
            } else {
                Minecraft.getInstance().gameRenderer.shutdownEffect();
            }
        }
    }
}
