package com.leventsurer.manager.data.repository

interface SharedRepository {
    fun writeApartmentName(key:String, value:String)
    fun writeUserName(key:String,value: String)
    fun writeUserDocumentId(key:String,value: String)
    fun writeIsLogin(key: String,value: Boolean)
    fun writeApartmentDocumentId(key: String,value: String)
    fun writeUserRole(key: String,value: String)

    fun readApartmentName(key: String): String?
    fun readUserName(key: String):String?
    fun readUserDocumentId(key: String):String?
    fun readIsLogin(key: String):Boolean?
    fun readApartmentDocumentId(key: String):String?

    fun removeValue(key: String)
    fun clearSharedPref()
}