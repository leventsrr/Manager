package com.leventsurer.manager.viewModels

import android.util.Log
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

    private val _managerAnnouncementFlow = MutableStateFlow<Resource<ArrayList<ManagerAnnouncementModel>>?>(null)
    val managerAnnouncementFlow : StateFlow<Resource<ArrayList<ManagerAnnouncementModel>>?> = _managerAnnouncementFlow

    private val _conciergeDutiesFlow = MutableStateFlow<Resource<ArrayList<ConciergeDutiesModel>>?>(null)
    val conciergeDutiesFlow : StateFlow<Resource<ArrayList<ConciergeDutiesModel>>?> = _conciergeDutiesFlow

    private val _residentRequestFlow = MutableStateFlow<Resource<ArrayList<ResidentsRequestModel>>?>(null)
    val residentRequestFlow : StateFlow<Resource<ArrayList<ResidentsRequestModel>>?> = _residentRequestFlow

    private val _financialEventsFlow = MutableStateFlow<Resource<ArrayList<FinancialEventModel>>?>(null)
    val financialEventsFlow : StateFlow<Resource<ArrayList<FinancialEventModel>>?> = _financialEventsFlow

    private val _userInfoFlow = MutableLiveData<Resource<UserModel>?>(null)
    val userInfoFlow : LiveData<Resource<UserModel>?> = _userInfoFlow

    private val _users = MutableStateFlow<Resource<ArrayList<UserModel>>?>(null)
    val users : StateFlow<Resource<ArrayList<UserModel>>?> = _users

    private val _chatMessagesFlow = MutableLiveData<LiveData<List<ChatMessageModel>>>(null)
    val chatMessagesFlow : LiveData<LiveData<List<ChatMessageModel>>?> = _chatMessagesFlow

    private val _apartmentLiveData = MutableLiveData<Resource<ApartmentModel>?>(null)
    val apartmentLiveData :LiveData<Resource<ApartmentModel>?> = _apartmentLiveData

    private val _apartmentsFlow = MutableStateFlow<Resource<List<ApartmentModel>>?>(null)
    val apartmentsFlow : StateFlow<Resource<List<ApartmentModel>>?> = _apartmentsFlow

    private val _pollsLiveData = MutableLiveData<Resource<List<PollModel>>?>(null)
    val pollsLiveData : LiveData<Resource<List<PollModel>>?> = _pollsLiveData

     suspend fun getPolls()  : LiveData<Resource<List<PollModel>>> {
        /*_pollsLiveData.value = Resource.Loading
        val result = databaseRepository.getPolls()
        _pollsLiveData.value = result*/
        return databaseRepository.getPolls()
    }

    fun addNewPoll(pollText:String,time: FieldValue) = viewModelScope.launch {
        databaseRepository.addNewPoll(pollText, time)
    }
    suspend fun changePollStatistics(isAgree: Boolean, pollText: String):String  {
        return databaseRepository.changePollStatistics(isAgree, pollText)
    }
    fun getApartmentInfo()= viewModelScope.launch {
        _apartmentLiveData.value = Resource.Loading
        val result = databaseRepository.getAnApartment()
        _apartmentLiveData.value = result
    }

    fun getAllApartments() = viewModelScope.launch {
        _apartmentsFlow.value = Resource.Loading
        val result = databaseRepository.getApartments()
        _apartmentsFlow.value = result
    }


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

    fun getManagerAnnouncement()= viewModelScope.launch {
        _managerAnnouncementFlow.value = Resource.Loading
        val result = databaseRepository.getManagerAnnouncements()
        _managerAnnouncementFlow.value = result

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

    fun addNewManagerAnnouncement(announcement:String,time:FieldValue) = viewModelScope.launch {
        databaseRepository.addNewManagerAnnouncement(announcement,time)
    }

    fun addNewConciergeAnnouncement(announcement:String,time:FieldValue) = viewModelScope.launch {
        databaseRepository.addNewConciergeAnnouncement(announcement,time)
    }
    fun changeConciergeDutyStatus(duty:String) = viewModelScope.launch {
        databaseRepository.changeConciergeDutyStatus(duty)
    }
    fun setUserDuesPaymentStatus(currentStats:Boolean,userName: String) = viewModelScope.launch {
        databaseRepository.changeUserDuesPaymentStatus(currentStats,userName)
    }

    fun addBudgetMovement(amount:Double,isExpense:Boolean,time:FieldValue,eventName:String) = viewModelScope.launch{
        databaseRepository.addNewFinancialEvent(amount,isExpense,time,eventName)
    }

    fun setApartmentMonthlyPayment(amount:Double) = viewModelScope.launch {
        databaseRepository.setApartmentMonthlyPayment(amount)
    }

    fun updateUserInfo(userName: String,phoneNumber:String,carPlate: String,doorNumber: String) = viewModelScope.launch {
        databaseRepository.updateUserInfo(userName, phoneNumber, carPlate, doorNumber)
    }
    fun addNewConciergeDuty(duty: String,time: FieldValue) = viewModelScope.launch {
        databaseRepository.addNewConciergeDuty(duty, time)
    }
    fun deleteUserData(){
        databaseRepository.deleteUserData()
    }
    fun resetData(isRequestReset: Boolean,
                  isManagerAnnouncementReset: Boolean,
                  isConciergeAnnouncementReset: Boolean,
                  isPollReset: Boolean,
                  isFinancialEventReset: Boolean,
    isConciergeDutyReset:Boolean)=viewModelScope.launch{
        databaseRepository.resetData(isRequestReset, isManagerAnnouncementReset, isConciergeAnnouncementReset, isPollReset, isFinancialEventReset,isConciergeDutyReset)
    }
}