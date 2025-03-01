package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.mixin;

import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserJoin;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author gdrfgdrf
 */
@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(at = @At("RETURN"), method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V")
    public void onPlayerConnectMixin(
            ClientConnection connection,
            ServerPlayerEntity player,
            CallbackInfo callbackInfo
    ) {
        String username = player.getName().getString();
        UserJoin.INSTANCE.timeout(username);
    }

}
