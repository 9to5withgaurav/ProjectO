package com.example.projecto

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


object HashGeneratorUtils {
    // Replace with your actual salt obtained from PayU
    private const val SALT = "chbbutJVeilAGz9jssS1K8gWbQWTEAvY"

    // Generate hash using SHA-512 algorithm
    fun generateHashFromSdk(
        key: String, txnId: String, amount: String, productInfo: String,
        firstName: String, email: String,
    ): String {
        val hashSequence = (key + "|" + txnId + "|" + amount + "|" + productInfo + "|"
                + firstName + "|" + email + "|" +  "||||||" + SALT)
        return hashCal(hashSequence)
    }

    // Actual hash calculation method (SHA-512)
    private fun hashCal(hashString: String): String {
        val hash = StringBuilder()
        try {
            val digest = MessageDigest.getInstance("SHA-512")
            val messageDigest = digest.digest(hashString.toByteArray(StandardCharsets.UTF_8))
            for (b in messageDigest) {
                hash.append(String.format("%02x", b))
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return hash.toString()
    }
}

