package ru.android.github.data.converter

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.nio.charset.Charset
import java.security.Key
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec


class Security {

    companion object {
        private const val KEY_ALIAS = "key"
        private const val IV = "muchsecure"
    }

    private val keyStore = KeyStore.getInstance("AndroidKeyStore")

    private val cipher = Cipher.getInstance("AES/GCM/NoPadding")

    fun encryptString(string: String): String {
        keyStore.load(null)

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                    KeyGenParameterSpec.Builder(KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setRandomizedEncryptionRequired(false)
                            .build())
            keyGenerator.generateKey()
        }

        cipher.init(Cipher.ENCRYPT_MODE, getKey(), GCMParameterSpec(128, IV.toByteArray()))
        val cipherText = cipher.doFinal(string.toByteArray(Charset.forName("UTF-8")))
        return Base64.encodeToString(cipherText, Base64.DEFAULT)

    }

    fun decryptString(string: String): String {
        cipher.init(Cipher.DECRYPT_MODE, getKey(), GCMParameterSpec(128, IV.toByteArray()))
        val decoded = Base64.decode(string, Base64.DEFAULT)
        return String(cipher.doFinal(decoded), Charset.forName("UTF-8"))
    }

    private fun getKey(): Key {
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null)
    }
}