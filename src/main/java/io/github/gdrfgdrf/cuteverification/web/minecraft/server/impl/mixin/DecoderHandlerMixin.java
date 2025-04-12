package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.gdrfgdrf.cuteverification.web.mediator.enums.IdentificationPlatforms;
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserJoin;
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.utils.Encryption;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.DecoderHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author gdrfgdrf
 */
@Mixin(DecoderHandler.class)
public class DecoderHandlerMixin {
    @Unique
    Integer CUSTOM_PACKET_ID = 56178;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/network/NetworkState;getPacketHandler(Lnet/minecraft/network/NetworkSide;I)Lnet/minecraft/network/Packet;", shift = At.Shift.BEFORE), method = "Lnet/minecraft/network/DecoderHandler;decode(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V", cancellable = true)
    public void decode_mixin(
            ChannelHandlerContext channelHandlerContext,
            ByteBuf byteBuf,
            List<Object> list,
            CallbackInfo callbackInfo,
            @Local(index = 5) int i) {
        if (i != CUSTOM_PACKET_ID) {
            return;
        }
        callbackInfo.cancel();

        ClientConnection clientConnection = (ClientConnection) channelHandlerContext.pipeline().get("packet_handler");
        ServerPlayNetworkHandler serverLoginNetworkHandler = (ServerPlayNetworkHandler) clientConnection.getPacketListener();
        ServerPlayerEntity entity = serverLoginNetworkHandler.player;

        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        byte[] publicKey = "test_key_1234567".getBytes(StandardCharsets.UTF_8);

        // 不能将 bytes 转成 String 再使用 split，会破坏原 bytes 数组
        List<byte[]> split = Encryption.INSTANCE.splitByteArray(bytes, (byte) ',');
        if (split.size() != 2) {
            return;
        }

        byte[] encryptedCodeBytes = split.get(0);
        byte[] encryptedPlatformBytes = split.get(1);

        String code = new String(Encryption.INSTANCE.decrypt(encryptedCodeBytes, publicKey, publicKey), StandardCharsets.UTF_8);
        String platform = new String(Encryption.INSTANCE.decrypt(encryptedPlatformBytes, publicKey, publicKey), StandardCharsets.UTF_8);
        String username = entity.getName().getString();
        String ip = clientConnection.getAddress().toString();

        UserJoin.INSTANCE.call(username, code, IdentificationPlatforms.valueOf(platform), ip);
    }

}
