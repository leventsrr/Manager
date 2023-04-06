package com.leventsurer.manager.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FieldValue
import com.leventsurer.manager.data.model.*


interface DatabaseRepository {
    //Get
    suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>>
    suspend fun getManagerAnnouncements(): Resource<ArrayList<ManagerAnnouncementModel>>
    suspend fun getConciergeDuties(): Resource<ArrayList<ConciergeDutiesModel>>
    suspend fun getRecentFinancialEvents(): Resource<ArrayList<FinancialEventModel>>
    suspend fun getResidentsRequests(): Resource<ArrayList<ResidentsRequestModel>>
    suspend fun getUsers(): Resource<ArrayList<UserModel>>
    suspend fun getAUser(): Resource<UserModel>


    suspend fun getApartments():Resource<List<Apartment>>
    suspend fun getAnApartment() : Resource<Apartment>

    suspend fun getAUserByNameAndDoorNumber(userName:String,doorNumber:String): Resource<UserModel>
    suspend fun getApartmentDocumentId(apartmentCode: String):String
    suspend fun writeUserDocumentIdToSharedPref(userName:String, apartmentCode: String):String
    fun getChatMessages():LiveData<Resource<List<ChatMessageModel>>>

    //Add
    suspend fun addNewUser(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String
    )



    suspend fun addNewUserToNewApartment(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String, documentId: String
    )

    suspend fun addNewApartment(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String, apartmentName: String
    )

    suspend fun changeUserDuesPaymentStatus(currentStatus:Boolean)

    suspend fun addNewRequest(request:String,time:FieldValue)
    suspend fun addNewManagerAnnouncement(announcement:String,time: FieldValue)
    suspend fun addNewFinancialEvent(amount:Double, isExpense:Boolean, time:FieldValue,eventName:String)
    suspend fun sendNewMessageInChat(message:String,userName:String,time:FieldValue)
    suspend fun setApartmentMonthlyPayment(amount:Double)


    //Set
    suspend fun updateAnUser(userName:String,phoneNumber: String,carPlate: String,doorNumber: String)
}