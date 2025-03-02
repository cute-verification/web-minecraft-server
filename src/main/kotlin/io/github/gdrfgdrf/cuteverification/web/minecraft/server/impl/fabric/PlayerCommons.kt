package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText

fun ServerPlayerEntity.kick() {
    this.networkHandler.connection.disconnect(LiteralText(""))
}