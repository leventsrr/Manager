package com.leventsurer.manager.data.repository

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.tools.constants.FirebaseConstants.APARTMENT_COLLECTIONS
import com.leventsurer.manager.tools.constants.FirebaseConstants.CONCIERGE_ANNOUNCEMENT
import com.leventsurer.manager.tools.constants.FirebaseConstants.DUTIES
import com.leventsurer.manager.tools.constants.FirebaseConstants.FINANCIAL_EVENTS
import com.leventsurer.manager.tools.constants.FirebaseConstants.RESIDENT_REQUESTS
import com.leventsurer.manager.tools.constants.FirebaseConstants.USER_COLLECTION
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_DOCUMENT_ID
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_NAME
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_DOCUMENT_ID
import kotlinx.coroutines.runBlocking

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : DatabaseRepository {
    @Inject
    lateinit var sharedRepository: SharedRepositoryImpl

    //Kapıcı duyurularının veri tabanından alınması
    override suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val announcements = arrayListOf<ConciergeAnnouncementModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        CONCIERGE_ANNOUNCEMENT
                    ).get().await()

            for (document in result) {
                announcements.add(document.toObject(ConciergeAnnouncementModel::class.java))
            }
            Resource.Success(announcements)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Kapıcı görevlerinin veri tabanından alınması
    override suspend fun getConciergeDuties(): Resource<ArrayList<ConciergeDutiesModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val duties = arrayListOf<ConciergeDutiesModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        DUTIES
                    ).get().await()

            for (document in result) {
                duties.add(document.toObject(ConciergeDutiesModel::class.java))
            }
            Resource.Success(duties)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Ekonomik işlemlerin veri tabanından alınması
    override suspend fun getRecentFinancialEvents(): Resource<ArrayList<FinancialEventModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val financialEvents = arrayListOf<FinancialEventModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        FINANCIAL_EVENTS
                    ).get().await()

            for (document in result) {
                financialEvents.add(document.toObject(FinancialEventModel::class.java))
            }
            Resource.Success(financialEvents)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Apartman sakinlerinin isteklerinin veri tabanından alınması
    override suspend fun getResidentsRequests(): Resource<ArrayList<ResidentsRequestModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val requests = arrayListOf<ResidentsRequestModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        RESIDENT_REQUESTS
                    ).get().await()

            for (document in result) {
                requests.add(document.toObject(ResidentsRequestModel::class.java))
            }
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getUsers(): ArrayList<UserModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getAUser(): Resource<UserModel> {
        return try {
            val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
            val userDocumentId = sharedRepository.readUserDocumentId(USER_DOCUMENT_ID)
            val userDocument: DocumentSnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
                    .collection(
                        USER_COLLECTION
                    ).document(userDocumentId!!).get().await()
            val userInfoModel = userDocument.toObject(UserModel::class.java)!!
            Log.e("kontrol"," kullanıcı adı ${userInfoModel.fullName}")
            Resource.Success(userInfoModel)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Daha önce kaydedilen apartmana yeni kullanıcı eklenmesi
    override suspend fun addNewUser(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String
    ) {
        val user = hashMapOf(
            "carPlate" to carPlate,
            "doorNumber" to doorNumber,
            "duesPaymentStatus" to false,
            "fullName" to name,
            "phoneNumber" to "",
            "role" to role,
            "imageLink" to ""
        )

        val apartmentsCollection: QuerySnapshot =
            database.collection(APARTMENT_COLLECTIONS).get().await()
        var documentId: String
        for (apartmentDocument: DocumentSnapshot in apartmentsCollection) {
            runBlocking {
                if (apartmentDocument.data?.get("apartmentName") as String == apartmentCode) {
                    documentId = apartmentDocument.reference.id

                    database.collection("apartments").document(documentId)
                        .collection(USER_COLLECTION).add(user).await()
                }
            }

        }

    }

    //Yeni açılan apartmana yeni kullanıcı kaydedilmesi
    override suspend fun addNewUserToNewApartment(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String,
        documentId: String
    ) {
        val user = hashMapOf(
            "carPlate" to carPlate,
            "doorNumber" to doorNumber,
            "duesPaymentStatus" to false,
            "fullName" to name,
            "phoneNumber" to "",
            "role" to role,
            "imageLink" to ""
        )
        val documentPath = "apartments"
        database.collection(documentPath).document(documentId).collection(USER_COLLECTION).add(user)
            .await()
    }

    //Veri tabanına yeni apartman kaydedilmesi
    override suspend fun addNewApartment(
        name: String,
        apartmentCode: String,
        carPlate: String,
        doorNumber: String,
        role: String, apartmentName: String
    ) {
        val result = database.collection(APARTMENT_COLLECTIONS).add(
            hashMapOf(
                "apartmentName" to apartmentName
            )
        ).await().id
        addNewUserToNewApartment(name, apartmentCode, carPlate, doorNumber, role, result)
    }

    //Grilen apartman adına göre apartmanın veri tabanındaki id sinin getirilmesi ve sharedPreferences a kaydedilmesi
    override suspend fun getApartmentDocumentId(apartmentCode: String): String {
        val apartmentsCollection: QuerySnapshot =
            database.collection(APARTMENT_COLLECTIONS).get().await()
        var documentId = ""
        for (apartmentDocument: DocumentSnapshot in apartmentsCollection) {
            runBlocking {
                if (apartmentDocument.data?.get("apartmentName") as String == apartmentCode) {
                    documentId = apartmentDocument.reference.id
                }
            }

        }
        sharedRepository.writeApartmentDocumentId(APARTMENT_DOCUMENT_ID, documentId)
        return documentId
    }

    //Giriş yapan kullanıcın verilerini gösterebilmek için firestore document id verisi alınır.
    override suspend fun getUserDocumentId(userName: String, apartmentCode: String): String {
        val apartmentsCollection: QuerySnapshot =
            database.collection(APARTMENT_COLLECTIONS).get().await()
        var apartmentDocumentId = ""
        var userDocumentId = ""
        for (apartmentDocument: DocumentSnapshot in apartmentsCollection) {
            runBlocking {
                if (apartmentDocument.data?.get("apartmentName") as String == apartmentCode) {
                    apartmentDocumentId = apartmentDocument.reference.id
                    val users: QuerySnapshot =
                        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId)
                            .collection(
                                USER_COLLECTION
                            ).get().await()
                    for (userDocument: DocumentSnapshot in users) {
                        runBlocking {
                            if (userDocument.data?.get("fullName") as String == userName) {
                                userDocumentId = userDocument.reference.id
                                sharedRepository.writeUserDocumentId(
                                    USER_DOCUMENT_ID,
                                    userDocumentId
                                )
                            }
                        }
                    }

                }
            }

        }
        return userDocumentId
    }

    //Apartman id sine ulaşmak için kullanılacak apartman adının shared preferencesten çekilmesi.
    private suspend fun reachToDocumentIdFromSharedPref(): String {
        val apartmentName = sharedRepository.readApartmentName(APARTMENT_NAME)
        return getApartmentDocumentId(apartmentName!!)
    }
}