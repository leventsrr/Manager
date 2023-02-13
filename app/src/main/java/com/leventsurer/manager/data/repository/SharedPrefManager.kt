package com.leventsurer.manager.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class SharedPrefManager @Inject constructor(
    @ApplicationContext private val context: Context
) {


    fun getSharedPref(): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "app",
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(): MasterKey {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MasterKey.Builder(context)
                //.setKeyGenParameterSpec(getKeyGenerator(application)) // Api 23 ve üzeri keygen üreterek şifreleme yap
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } else {
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        }
    }
}
