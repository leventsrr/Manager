package com.leventsurer.manager.data.repository

import com.leventsurer.manager.data.model.*


interface DatabaseRepository {
    //Get
    suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>>
    suspend fun getConciergeDuties(): Resource<ArrayList<ConciergeDutiesModel>>
    suspend fun getRecentFinancialEvents(): Resource<ArrayList<FinancialEventModel>>
    suspend fun getResidentsRequests(): Resource<ArrayList<ResidentsRequestModel>>
    suspend fun getUsers(): ArrayList<UserModel>
    suspend fun getAUser(fullName: String, doorNumber: String): UserModel
    suspend fun getApartmentDocumentId(apartmentCode: String):String
    suspend fun getUserDocumentId(userName:String,apartmentCode: String):String
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
}