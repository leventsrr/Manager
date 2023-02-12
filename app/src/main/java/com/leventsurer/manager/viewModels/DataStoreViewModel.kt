package com.leventsurer.manager.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.repository.DataStoreRepository
import com.leventsurer.manager.tools.constants.DataStoreConstants.APARTMENT_NAME
import com.leventsurer.manager.tools.constants.DataStoreConstants.IS_LOGIN
import com.leventsurer.manager.tools.constants.DataStoreConstants.USER_NAME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepository:DataStoreRepository
) :ViewModel(){

    private val _userNameFlow = MutableStateFlow<Resource<String?>?>(null)
    val userNameFlow : StateFlow<Resource<String?>?> = _userNameFlow

    private val _apartmentCodeFlow = MutableStateFlow<Resource<String?>?>(null)
    val apartmentCodeFlow : StateFlow<Resource<String?>?> = _apartmentCodeFlow

    private val _isLoginFlow = MutableStateFlow<Resource<Boolean?>?>(null)
    val isLoginFlow : StateFlow<Resource<Boolean?>?> = _isLoginFlow

     fun storeUserName(value:String) = viewModelScope.launch {
        dataStoreRepository.putUserName(USER_NAME,value)
    }
     fun getUserName() = runBlocking{
        _userNameFlow.value = Resource.Loading
        val result = dataStoreRepository.getUserName(USER_NAME)
        _userNameFlow.value = result
    }

     fun storeApartmentCode(value:String) = runBlocking {
        dataStoreRepository.putApartmentCode(APARTMENT_NAME,value)

    }
     fun getApartmentCode() = viewModelScope.launch{
        _apartmentCodeFlow.value = Resource.Loading
        val result = dataStoreRepository.getUserName(APARTMENT_NAME)
        _apartmentCodeFlow.value = result
    }


     fun storeIsLogin(value:Boolean) = runBlocking {
        dataStoreRepository.putIsLogin(IS_LOGIN,value)

    }
     fun getIsLogin() = viewModelScope.launch{
        _isLoginFlow.value = Resource.Loading
        val result = dataStoreRepository.getIsLogin(IS_LOGIN)
        _isLoginFlow.value = result
    }

}