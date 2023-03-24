package com.leventsurer.manager.data.repository

import com.google.firebase.firestore.FieldValue
import com.leventsurer.manager.data.model.*


interface DatabaseRepository {
    //Get
    suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>>
    suspend fun getConciergeDuties(): Resource<ArrayList<ConciergeDutiesModel>>
    suspend fun getRecentFinancialEvents(): Resource<ArrayList<FinancialEventModel>>
    suspend fun getResidentsRequests(): Resource<ArrayList<ResidentsRequestModel>>
    suspend fun getUsers(): Resource<ArrayList<UserModel>>
    suspend fun getAUser(): Resource<UserModel>
    suspend fun getAUserByNameAndDoorNumber(userName:String,doorNumber:String): Resource<UserModel>
    suspend fun getApartmentDocumentId(apartmentCode: String):String
    suspend fun getUserDocumentId(userName:String,apartmentCode: String):String
    suspend fun getChatMessages():Resource<List<ChatMessageModel>>

    //Set
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

    suspend fun addNewRequest(request:String)

    suspend fun sendNewMessageInChat(message:String,userName:String,time:FieldValue)

}