package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.bean

import io.github.gdrfgdrf.cuteverification.web.mediator.enums.IdentificationPlatforms
import io.netty.buffer.ByteBuf
import java.nio.charset.StandardCharsets

class IdentificationDTO {
    var code: String? = null

    companion object {
        fun read(byteBuf: ByteBuf): IdentificationDTO {
            val length = byteBuf.readInt()
            val encrypted_code = byteBuf.readCharSequence(length, StandardCharsets.UTF_8)

            val result = IdentificationDTO()
            result.code = encrypted_code as String

            return result
        }
    }
}