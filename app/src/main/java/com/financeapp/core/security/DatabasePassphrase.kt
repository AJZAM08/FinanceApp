@file:Suppress("DEPRECATION")

package com.financeapp.core.security

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabasePassphrase @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    companion object {
        private const val PREF_NAME = "secure_db_prefs"
        private const val KEY_PASSPHRASE = "db_passphrase"
        private const val PASSPHRASE_LENGTH = 32
    }

    private val sharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun getOrCreatePassphrase(): ByteArray {
        val encodedPassphrase = sharedPreferences.getString(KEY_PASSPHRASE, null)
        return if (encodedPassphrase != null) {
            android.util.Base64.decode(encodedPassphrase, android.util.Base64.DEFAULT)
        } else {
            val passphrase = ByteArray(PASSPHRASE_LENGTH)
            SecureRandom().nextBytes(passphrase)
            val newEncodedPassphrase = android.util.Base64.encodeToString(passphrase, android.util.Base64.DEFAULT)
            sharedPreferences.edit {
                putString(KEY_PASSPHRASE, newEncodedPassphrase)
            }
            passphrase
        }
    }
}
