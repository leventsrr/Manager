package com.leventsurer.manager.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
): ViewModel(){

    private val _conciergeAnnouncementFlow = MutableStateFlow<Resource<ArrayList<ConciergeAnnouncementModel>>?>(null)
    val conciergeAnnouncementFlow : StateFlow<Resource<ArrayList<ConciergeAnnouncementModel>>?> = _conciergeAnnouncementFlow

    private val _conciergeDutiesFlow = MutableStateFlow<Resource<ArrayList<ConciergeDutiesModel>>?>(null)
    val conciergeDutiesFlow : StateFlow<Resource<ArrayList<ConciergeDutiesModel>>?> = _conciergeDutiesFlow

    private val _residentRequestFlow = MutableStateFlow<Resource<ArrayList<ResidentsRequestModel>>?>(null)
    val residentRequestFlow : StateFlow<Resource<ArrayList<ResidentsRequestModel>>?> = _residentRequestFlow

    private val _financialEventsFlow = MutableStateFlow<Resource<ArrayList<FinancialEventModel>>?>(null)
    val financialEventsFlow : StateFlow<Resource<ArrayList<FinancialEventModel>>?> = _financialEventsFlow

    private val _userInfoFlow = MutableStateFlow<Resource<UserModel>?>(null)
    val userInfoFlow : StateFlow<Resource<UserModel>?> = _userInfoFlow

    fun getUserInfo() = viewModelScope.launch {

        _userInfoFlow.value = Resource.Loading
        val result = databaseRepository.getAUser()
        _userInfoFlow.value = result
    }


    fun getFinancialEvents()= viewModelScope.launch {
        _financialEventsFlow.value = Resource.Loading
        val result = databaseRepository.getRecentFinancialEvents()
        _financialEventsFlow.value = result
    }

    fun getConciergeAnnouncement()= viewModelScope.launch {
        _conciergeAnnouncementFlow.value = Resource.Loading
        val result = databaseRepository.getConciergeAnnouncements()
        _conciergeAnnouncementFlow.value = result

    }

    fun getResidentRequests() = viewModelScope.launch {
        _residentRequestFlow.value = Resource.Loading
        val result = databaseRepository.getResidentsRequests()
        _residentRequestFlow.value = result

    }

    fun getConciergeDuties() = viewModelScope.launch {
        _conciergeDutiesFlow.value = Resource.Loading
        val result = databaseRepository.getConciergeDuties()
        _conciergeDutiesFlow.value = result
    }


    fun addNewUser(name:String,apartmentCode:String,carPlate:String,doorNumber:String,role:String) = viewModelScope.launch {
        databaseRepository.addNewUser(name, apartmentCode, carPlate , doorNumber,role)
    }

    fun getUserDocumentId(userName:String,apartmentCode: String)  = viewModelScope.launch {
        databaseRepository.getUserDocumentId(userName,apartmentCode)
    }

    fun addNewApartment(name: String,
                        apartmentCode: String,
                        carPlate: String,
                        doorNumber: String,
                        role: String, apartmentName: String) = viewModelScope.launch {
        databaseRepository.addNewApartment(name,apartmentCode,carPlate,doorNumber, role, apartmentName)
    }

    fun addNewRequest(request:String) = viewModelScope.launch {
        databaseRepository.addNewRequest(request)
    }

    fun setUserDuesPaymentStatus(currentStats:Boolean) = viewModelScope.launch {
        Log.e("kontrol","viewmodel içinde $currentStats")
        databaseRepository.changeUserDuesPaymentStatus(currentStats)
    }
}