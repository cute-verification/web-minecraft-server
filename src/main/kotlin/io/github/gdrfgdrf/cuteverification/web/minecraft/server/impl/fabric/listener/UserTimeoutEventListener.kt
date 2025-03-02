package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.listener

import com.google.common.eventbus.Subscribe
import io.github.gdrfgdrf.cuteverification.web.mediator.event.bus.EventCenter
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserTimeoutEvent
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.ServerMain
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.kick
import net.minecraft.text.LiteralText

object UserTimeoutEventListener {
    fun register() {
        EventCenter.register(this)
    }

    @Subscribe
    fun onUserTimeout(userTimeoutEvent: UserTimeoutEvent) {
        val username = userTimeoutEvent.username
        val playerManager = ServerMain.serverInstance!!.playerManager
        val serverPlayerEntity = playerManager.getPlayer(username) ?: return

        serverPlayerEntity.kick()
    }
}