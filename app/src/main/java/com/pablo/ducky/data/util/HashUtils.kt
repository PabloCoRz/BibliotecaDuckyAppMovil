package com.pablo.ducky.util

import java.security.MessageDigest

object HashUtils {
    /** Retorna el hash SHA-256 del string como string hexadecimal. */
    fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
