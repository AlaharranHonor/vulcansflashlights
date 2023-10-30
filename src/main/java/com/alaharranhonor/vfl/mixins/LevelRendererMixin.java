package com.alaharranhonor.vfl.mixins;

import com.alaharranhonor.vfl.client.ClientEventHandler;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @Inject(method = "resize", at = @At("TAIL"))
    private void resizeFlashlightEffect(int pWidth, int pHeight, CallbackInfo ci) {
        if (ClientEventHandler.flashlightEffect != null) {
            ClientEventHandler.flashlightEffect.resize(pWidth, pHeight);
        }
    }
}
