package com.leventsurer.manager.data.repository

import com.leventsurer.manager.data.model.*


interface DatabaseRepository {
     suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>>
    suspend fun getConciergeDuties():Resource<ArrayList<ConciergeDutiesModel>>
    suspend fun getRecentFinancialEvents(): Resource<ArrayList<FinancialEventModel>>
    suspend fun getResidentsRequests():Resource<ArrayList<ResidentsRequestModel>>
    suspend fun getUsers():ArrayList<UserModel>
    suspend fun getAUser(fullName:String,doorNumber:String):UserModel
}