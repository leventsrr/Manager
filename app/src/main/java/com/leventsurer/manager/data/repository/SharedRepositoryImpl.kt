package com.leventsurer.manager.data.repository

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject


class SharedRepositoryImpl @Inject constructor(
) : SharedRepository {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun writeApartmentName(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun writeUserName(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun writeIsLogin(key: String, value: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    override fun writeApartmentDocumentId(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun writeUserDocumentId(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }


    override fun writeUserRole(key: String, value: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readUserRole(key: String):String? {
        return sharedPreferences.getString(key,"")
    }
    override fun readApartmentName(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun readUserName(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun readIsLogin(key: String): Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun readApartmentDocumentId(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    override fun readUserDocumentId(key: String): String? {
        return sharedPreferences.getString(key,"")
    }
    override fun removeValue(key: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun clearSharedPref() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}