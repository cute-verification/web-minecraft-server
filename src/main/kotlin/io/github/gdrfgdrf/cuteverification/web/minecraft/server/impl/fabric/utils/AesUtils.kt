package io.github.gdrfgdrf.cuteverification.web.minecraft.server.impl.fabric.utils

import java.security.Security
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AesUtils {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CFB8/NoPadding"

    fun decrypt2String(content: String, key: String, iv: String): String {
        val result = decrypt2ByteArray(content, key, iv)

        return String(result, Charsets.UTF_8)
    }

    fun decrypt2ByteArray(content: String, key: String, iv: String): ByteArray {
        return decrypt(
            content.toByteArray(Charsets.UTF_8),
            key.toByteArray(Charsets.UTF_8),
            iv.toByteArray(Charsets.UTF_8)
        )
    }

    fun decrypt(content: ByteArray, key: ByteArray, iv: ByteArray): ByteArray {
        val secretKey = SecretKeySpec(key, ALGORITHM)
        val ivSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)

        val decrypted = cipher.doFinal(content)
        return decrypted
    }

    fun splitByteArray(byteArray: ByteArray, divider: Byte): List<ByteArray> {
        val dividerIndices = mutableListOf<Int>()
        byteArray.forEachIndexed { index, byte ->
            if (byte == divider) {
                dividerIndices.add(index)
            }
        }

        val result = arrayListOf<ByteArray>()
        var start = 0

        dividerIndices.forEach { index ->
            result.add(byteArray.copyOfRange(start, index))
            start = index + 1
        }

        if (start <= byteArray.lastIndex) {
            result.add(byteArray.copyOfRange(start, byteArray.size))
        }

        return result
    }
}