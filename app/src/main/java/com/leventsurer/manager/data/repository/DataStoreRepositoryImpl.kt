package com.leventsurer.manager.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.tools.constants.DataStoreConstants.DATASTORE_NAME
import kotlinx.coroutines.flow.first


import javax.inject.Inject

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

class DataStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : DataStoreRepository {
    override suspend fun putApartmentCode(key: String, value: String) {

        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun putUserName(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun putIsLogin(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.dataStore.edit {
            it[preferencesKey] = value
        }
    }

    override suspend fun getApartmentName(key: String):Resource<String?> {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
             Resource.Success(preference[preferencesKey])
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getUserName(key: String): Resource<String?> {
        return try {
            val preferencesKey = stringPreferencesKey(key)
            val preference = context.dataStore.data.first()
            Resource.Success(preference[preferencesKey])

        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun getIsLogin(key: String): Resource<Boolean?> {
        return try {
            val preferencesKey = booleanPreferencesKey(key)
            val preference = context.dataStore.data.first()
            Resource.Success(preference[preferencesKey])
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun clearDataStore(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit {
            if(it.contains(preferencesKey)){
                it.remove(preferencesKey)
            }
        }
    }
}