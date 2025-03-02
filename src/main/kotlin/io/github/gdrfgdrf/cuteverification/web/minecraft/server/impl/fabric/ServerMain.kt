package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric

import io.github.gdrfgdrf.cuteverification.web.mediator.enums.IdentificationPlatforms
import io.github.gdrfgdrf.cuteverification.web.mediator.event.listener.UserJoinEventListener
import io.github.gdrfgdrf.cuteverification.web.mediator.event.listener.UserLoginSuccessEventListener
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.Compatible
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.IExtendedUser
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.IUserPool
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserLoginEventListener
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.compatible.events.UserRestrictedEventListener
import io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.listener.UserTimeoutEventListener
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import org.apache.logging.log4j.LogManager

object ServerMain : DedicatedServerModInitializer {
	var serverInstance: MinecraftServer? = null
	val logger = LogManager.getLogger("cute-verification-server")

	override fun onInitializeServer() {
		val environment = FabricLoader.getInstance().environmentType
		if (environment != EnvType.SERVER) {
			logger.info("cure-verification-server cannot load in client side")
			return
		}

		Compatible.start(object : IUserPool {
			override fun findUser(
				username: String,
				code: String,
				platform: IdentificationPlatforms,
				ip: String
			): IExtendedUser? {
				if (this@ServerMain.serverInstance == null) {
					return null
				}
				val playerManager = this@ServerMain.serverInstance!!.playerManager
				val serverPlayerEntity = playerManager.getPlayer(username) ?: return null

				return ExtendedUser.make(serverPlayerEntity, code, platform)
			}
		})

		ServerLifecycleEvents.SERVER_STARTED.register {
			this.serverInstance = it
		}

		NetworkingIdentifiers.registerAll()
		UserTimeoutEventListener.register()
		UserJoinEventListener
		UserLoginSuccessEventListener
		UserLoginEventListener
		UserRestrictedEventListener
	}
}