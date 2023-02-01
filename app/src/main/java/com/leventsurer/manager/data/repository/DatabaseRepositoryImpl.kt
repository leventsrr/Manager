package com.leventsurer.manager.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.tools.constants.FirebaseConstants.APARTMENT_COLLECTIONS
import com.leventsurer.manager.tools.constants.FirebaseConstants.CONCIERGE_ANNOUNCEMENT
import com.leventsurer.manager.tools.helpers.await
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val database:FirebaseFirestore,
):DatabaseRepository {


    override fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>> {
        val announcemets = ArrayList<ConciergeAnnouncementModel>()
        database.collection(APARTMENT_COLLECTIONS).document("mrpLL3uhAi35Kl4lSohj").collection(
            CONCIERGE_ANNOUNCEMENT).get().
                addOnSuccessListener { documents->

                    for(document in documents){
                        val announcement = document.toObject<ConciergeAnnouncementModel>()
                        announcemets.add(announcement)
                    }


                }
            .addOnFailureListener {

            }

        return  Resource.Success(announcemets)

    }

    override fun getConciergeDuties(): ArrayList<ConciergeDutiesModel> {
        TODO("Not yet implemented")
    }

    override fun getRecentFinancialEvents(): ArrayList<FinancialEventModel> {
        TODO("Not yet implemented")
    }

    override fun getResidentsRequests(): ArrayList<ResidentsRequest> {
        TODO("Not yet implemented")
    }

    override fun getUsers(): ArrayList<UserModel>{
        TODO("Not yet implemented")
    }

    override fun getAUser(fullName: String, doorNumber: String): UserModel {
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