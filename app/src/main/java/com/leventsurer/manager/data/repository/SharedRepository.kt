package com.leventsurer.manager.data.repository

interface SharedRepository {
    fun writeApartmentCode(key:String,value:String)
    fun writeUserName(key:String,value: String)
    fun writeIsLogin(key: String,value: Boolean)

    fun readApartmentName(key: String): String?
    fun readUserName(key: String):String?
    fun readIsLogin(key: String):Boolean?

    fun removeValue(key: String)
    fun clearSharedPref()
}