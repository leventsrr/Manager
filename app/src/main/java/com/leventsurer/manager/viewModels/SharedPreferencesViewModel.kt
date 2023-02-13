package com.leventsurer.manager.viewModels

import androidx.lifecycle.ViewModel
import com.leventsurer.manager.data.repository.SharedRepository
import com.leventsurer.manager.data.repository.SharedRepositoryImpl
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_NAME
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.IS_LOGIN
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedPreferencesViewModel @Inject constructor(
    private val sharedPrefRepository:SharedRepositoryImpl
) :ViewModel(){

    fun writeApartmentCode(value:String){
        sharedPrefRepository.writeApartmentCode(APARTMENT_NAME,value)
    }
    fun writeUserName(value: String){
        sharedPrefRepository.writeUserName(USER_NAME,value)
    }
    fun writeIsLogin(value: Boolean){
        sharedPrefRepository.writeIsLogin(IS_LOGIN,value)
    }

    fun readApartmentName(): String?{
       return sharedPrefRepository.readApartmentName(APARTMENT_NAME)
    }
    fun readUserName():String?{
       return sharedPrefRepository.readUserName(USER_NAME)
    }
    fun readIsLogin():Boolean?{
        return sharedPrefRepository.readIsLogin(IS_LOGIN)
    }

    fun removeValue(key: String){
        sharedPrefRepository.removeValue(key)
    }
    fun clearSharedPref(){
        sharedPrefRepository.clearSharedPref()
    }







}