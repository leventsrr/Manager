package com.leventsurer.manager.data.repository

import com.leventsurer.manager.data.model.Resource

interface DataStoreRepository {

    suspend fun putApartmentCode(key:String,value:String)
    suspend fun putUserName(key:String,value: String)
    suspend fun putIsLogin(key: String,value: Boolean)

    suspend fun getApartmentName(key: String):Resource<String?>
    suspend fun getUserName(key: String):Resource<String?>
    suspend fun getIsLogin(key: String):Resource<Boolean?>

    suspend fun clearDataStore(key: String)

}