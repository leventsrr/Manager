package com.leventsurer.manager.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.tools.constants.FirebaseConstants.APARTMENT_COLLECTIONS
import com.leventsurer.manager.tools.constants.FirebaseConstants.CONCIERGE_ANNOUNCEMENT
import com.leventsurer.manager.tools.constants.FirebaseConstants.DUTIES
import com.leventsurer.manager.tools.constants.FirebaseConstants.RESIDENT_REQUESTS

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : DatabaseRepository {


    override suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>> {
        return try {
            val announcements = arrayListOf<ConciergeAnnouncementModel>()
            val result:QuerySnapshot = database.collection(APARTMENT_COLLECTIONS).document("mrpLL3uhAi35Kl4lSohj").collection(
                CONCIERGE_ANNOUNCEMENT
            ).get().await()

            for (document in result){
                announcements.add(document.toObject(ConciergeAnnouncementModel::class.java))
            }
            Resource.Success(announcements)
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend  fun getConciergeDuties(): Resource<ArrayList<ConciergeDutiesModel>> {
        return try {
            val duties = arrayListOf<ConciergeDutiesModel>()
            val result:QuerySnapshot = database.collection(APARTMENT_COLLECTIONS).document("mrpLL3uhAi35Kl4lSohj").collection(
                DUTIES
            ).get().await()

            for (document in result){
                duties.add(document.toObject(ConciergeDutiesModel::class.java))
            }
            Resource.Success(duties)
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun getRecentFinancialEvents(): ArrayList<FinancialEventModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getResidentsRequests(): Resource<ArrayList<ResidentsRequestModel>> {
        return try {
            val requests = arrayListOf<ResidentsRequestModel>()
            val result:QuerySnapshot = database.collection(APARTMENT_COLLECTIONS).document("mrpLL3uhAi35Kl4lSohj").collection(
                RESIDENT_REQUESTS
            ).get().await()

            for (document in result){
                requests.add(document.toObject(ResidentsRequestModel::class.java))
            }
            Resource.Success(requests)
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun getUsers(): ArrayList<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAUser(fullName: String, doorNumber: String): UserModel {
        /* val docRef = database.collection(APARTMENT_COLLECTIONS).document().
         collection("users").document("MLq4h47uZ6ujTsyOR30G").get()
             .addOnSuccessListener { document ->
                 if(document != null){
                     Log.d("Kontrol","document info is ${document.data}")
                 }
             }
             .addOnFailureListener { exception->
                 Log.d("kontrol","get failed with ", exception)
             }*/
        TODO()
    }
}