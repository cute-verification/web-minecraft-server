package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric

import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserJoin
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.bean.IdentificationDTO
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf

object NetworkingIdentifiers {
    val identification = RegistrationPacket("identification") { _, serverPlayerEntity, _, byteBuf, _ ->
        runCatching {
            val identificationDto = IdentificationDTO.read(byteBuf)

            val username = serverPlayerEntity.name.string
            val code = identificationDto.code!!
//            val platform = identificationDto.platform!!
            val ip = serverPlayerEntity.networkHandler.connection.address.toString()

//            UserJoin.call(username, code, platform, ip)
        }.onFailure {
            ServerMain.logger.error("An error occurred when processing identification packet, kicking the player")
            serverPlayerEntity.kick()
        }
    }

    fun registerAll() {
        identification.register()
    }

    class RegistrationPacket(
        val identifier: String,
        val receiver: (MinecraftServer, ServerPlayerEntity, ServerPlayNetworkHandler, PacketByteBuf, PacketSender) -> Unit
    ) {
        fun register() {
            val channel = "cuteverification"
            val identifier = Identifier(channel, identifier)

            ServerPlayNetworking.registerGlobalReceiver(identifier, receiver)
        }
    }
}