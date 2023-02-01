package com.leventsurer.manager.data.repository

import com.leventsurer.manager.data.model.*


interface DatabaseRepository {
    fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>>
    fun getConciergeDuties():ArrayList<ConciergeDutiesModel>
    fun getRecentFinancialEvents():ArrayList<FinancialEventModel>
    fun getResidentsRequests():ArrayList<ResidentsRequest>
    fun getUsers():ArrayList<UserModel>
    fun getAUser(fullName:String,doorNumber:String):UserModel
}