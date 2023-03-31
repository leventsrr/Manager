package com.leventsurer.manager.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FieldValue
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.data.repository.DatabaseRepository
import com.leventsurer.manager.data.repository.SharedRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
): ViewModel(){
    @Inject
    lateinit var sharedRepository: SharedRepositoryImpl

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

    private val _users = MutableStateFlow<Resource<ArrayList<UserModel>>?>(null)
    val users : StateFlow<Resource<ArrayList<UserModel>>?> = _users

    private val _chatMessagesFlow = MutableLiveData<Resource<List<ChatMessageModel>>?>(null)
    val chatMessagesFlow : LiveData<Resource<List<ChatMessageModel>>?> = _chatMessagesFlow


    fun getChatMessages(): LiveData<Resource<List<ChatMessageModel>>>{
        return  databaseRepository.getChatMessages()
    }

    fun sendChatMessage(message:String,userName: String,time:FieldValue) = viewModelScope.launch {
        databaseRepository.sendNewMessageInChat(message,userName,time)
    }

    fun getUserInfo() = viewModelScope.launch {
        _userInfoFlow.value = Resource.Loading
        val result = databaseRepository.getAUser()
        _userInfoFlow.value = result
    }

    fun getAUserByNameAndDoorNumber(userName: String,doorNumber: String) = viewModelScope.launch {
        _userInfoFlow.value = Resource.Loading
        val result = databaseRepository.getAUserByNameAndDoorNumber(userName,doorNumber)
        _userInfoFlow.value = result
    }

    fun getAllApartmentUsers() = viewModelScope.launch {
        _users.value = Resource.Loading
        val result = databaseRepository.getUsers()
        _users.value = result
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

    fun writeUserDocumentIdToSharedPref(userName:String, apartmentCode: String)  = viewModelScope.launch {
        databaseRepository.writeUserDocumentIdToSharedPref(userName,apartmentCode)
    }

    fun addNewApartment(name: String,
                        apartmentCode: String,
                        carPlate: String,
                        doorNumber: String,
                        role: String, apartmentName: String) = viewModelScope.launch {
        databaseRepository.addNewApartment(name,apartmentCode,carPlate,doorNumber, role, apartmentName)
    }

    fun addNewRequest(request:String,time:FieldValue) = viewModelScope.launch {
        databaseRepository.addNewRequest(request,time)
    }

    fun setUserDuesPaymentStatus(currentStats:Boolean) = viewModelScope.launch {
        databaseRepository.changeUserDuesPaymentStatus(currentStats)
    }

    fun addBudgetMovement(amount:Double,isExpense:Boolean,time:FieldValue,eventName:String) = viewModelScope.launch{
        databaseRepository.addNewFinancialEvent(amount,isExpense,time,eventName)
    }

    fun setApartmentMonthlyPayment(amount:Double) = viewModelScope.launch {
        databaseRepository.setApartmentMonthlyPayment(amount)
    }

}