package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric

import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.IExtendedUser
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.enums.KickReasons
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

class ExtendedUser(
    override var id: String?,
    override var username: String,
    override var code: String,
    override var ip: String,
    val player: ServerPlayerEntity
) : IExtendedUser {
    override fun kick(reason: KickReasons) {
        ServerMain.logger.info("Kick player ${player.name.string}, reason: $reason")
        player.networkHandler?.disconnect(LiteralText(""))
    }

    companion object {
        fun make(serverPlayerEntity: ServerPlayerEntity, code: String): ExtendedUser {
            return ExtendedUser(
                null,
                serverPlayerEntity.name.string,
                code,
                serverPlayerEntity.networkHandler.connection.address.toString(),
                serverPlayerEntity
            )
        }
    }
}