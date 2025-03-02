package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.bean

import io.github.gdrfgdrf.cuteverification.web.mediator.enums.IdentificationPlatforms
import net.minecraft.util.PacketByteBuf

class IdentificationDTO {
    var code: String? = null
    var platform: IdentificationPlatforms? = null

    companion object {
        fun read(byteBuf: PacketByteBuf): IdentificationDTO {
            val code = byteBuf.readString()
            val platform = byteBuf.readEnumConstant(IdentificationPlatforms::class.java)

            val result = IdentificationDTO()
            result.code = code
            result.platform = platform

            return result
        }
    }
}