package com.leventsurer.manager.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.leventsurer.manager.data.model.*
import com.leventsurer.manager.tools.constants.FirebaseConstants.APARTMENT_COLLECTIONS
import com.leventsurer.manager.tools.constants.FirebaseConstants.CHAT_COLLECTION
import com.leventsurer.manager.tools.constants.FirebaseConstants.CONCIERGE_ANNOUNCEMENT
import com.leventsurer.manager.tools.constants.FirebaseConstants.DUTIES
import com.leventsurer.manager.tools.constants.FirebaseConstants.FINANCIAL_EVENTS
import com.leventsurer.manager.tools.constants.FirebaseConstants.MANAGER_ANNOUNCEMENT
import com.leventsurer.manager.tools.constants.FirebaseConstants.POLLS
import com.leventsurer.manager.tools.constants.FirebaseConstants.RESIDENT_REQUESTS
import com.leventsurer.manager.tools.constants.FirebaseConstants.USER_COLLECTION
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_DOCUMENT_ID
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_NAME
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_DOCUMENT_ID
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_NAME
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_ROLE
import kotlinx.coroutines.runBlocking

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
) : DatabaseRepository {
    @Inject
    lateinit var sharedRepository: SharedRepositoryImpl
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    //Kapıcı duyurularının veri tabanından alınması
    override suspend fun getConciergeAnnouncements(): Resource<ArrayList<ConciergeAnnouncementModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val announcements = arrayListOf<ConciergeAnnouncementModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        CONCIERGE_ANNOUNCEMENT
                    ).orderBy("time", Query.Direction.DESCENDING).get().await()

            for (document in result) {
                announcements.add(document.toObject(ConciergeAnnouncementModel::class.java))
            }
            Resource.Success(announcements)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Yönetici duyurularının veri tabanından alınması
    override suspend fun getManagerAnnouncements(): Resource<ArrayList<ManagerAnnouncementModel>> {
        val documentId = reachToDocumentIdFromSharedPref()
        return try {
            val announcements = arrayListOf<ManagerAnnouncementModel>()
            val result: QuerySnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(documentId)
                    .collection(
                        MANAGER_ANNOUNCEMENT
                    ).orderBy("time", Query.Direction.DESCENDING).get().await()

            for (document in result) {
                announcements.add(document.toObject(ManagerAnnouncementModel::class.java))
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
                    ).orderBy("date", Query.Direction.DESCENDING).get().await()

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
                    ).orderBy("time", Query.Direction.DESCENDING).get().await()

            for (document in result) {
                requests.add(document.toObject(ResidentsRequestModel::class.java))
            }
            Resource.Success(requests)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    override suspend fun getUsers(): Resource<ArrayList<UserModel>> {
        return try {
            val apartmentDocumentId =
                sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
            val usersDocuments =
                database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
                    .collection(
                        USER_COLLECTION
                    ).get().await()
            val users = arrayListOf<UserModel>()
            for (user in usersDocuments) {
                val userModel = user.toObject(UserModel::class.java)!!
                users.add(userModel)
            }
            Resource.Success(users)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Giriş yapan kullanıcın bilgilerini veri tabanından getirir
    override suspend fun getAUser(): Resource<UserModel> {
        return try {
            val apartmentDocumentId =
                sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
            val userDocumentId = sharedRepository.readUserDocumentId(USER_DOCUMENT_ID)
            val userDocument: DocumentSnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
                    .collection(
                        USER_COLLECTION
                    ).document(userDocumentId!!).get().await()
            val userInfoModel = userDocument.toObject(UserModel::class.java)!!
            sharedRepository.writeUserRole(USER_ROLE, userInfoModel.role)
            Resource.Success(userInfoModel)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
    //Apartmana ait anketleri veritabanından çeker
    override suspend fun getPolls(): Resource<List<PollModel>> {
        return try {
            val apartmentDocumentId =
                sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
            val polls = arrayListOf<PollModel>()
            val pollDocuments = database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(POLLS).get().await()
            for(doc in pollDocuments){
                Log.e("kontrol",doc.toString())
                polls.add(doc.toObject(PollModel::class.java))
            }
            Resource.Success(polls)
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun addNewPoll(pollText: String,time:FieldValue) {
        val apartmentDocumentId =
            sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val newPoll = hashMapOf(
            "agreeCount" to 0,
            "disagreeCount" to 0,
            "people" to mapOf("agreePeople" to arrayListOf<String>(),"disagreePeople" to arrayListOf<String>()),
            "pollText" to pollText,
            "time" to time
        )

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(POLLS).add(newPoll).await()
    }

    override suspend fun changePollStatistics(isAgree: Boolean,pollText: String) {
        val apartmentDocumentId =
            sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val userName: String? = sharedRepository.readUserName(USER_NAME)
        val pollDocument = database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(
            POLLS).whereEqualTo("pollText",pollText).get().await()

        val pollModel = pollDocument.documents[0].toObject(PollModel::class.java)
        Log.e("kontrol","isAgree değeri:$isAgree")
        if (userName.toString() !in pollModel!!.people["agreePeople"]!! && userName.toString() !in pollModel.people["disagreePeople"]!! ){
            if(isAgree){
                pollModel.agreeCount +=1
                pollModel.people["agreePeople"]?.add(userName!!)
            }else if(!isAgree){
                pollModel.disagreeCount +=1
                pollModel.people["disagreePeople"]?.add(userName!!)
            }

            database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId).collection(
                POLLS).document(pollDocument.documents[0].id).set(pollModel)
        }
    }
    //Tüm apartmanların listesini getirir
    override suspend fun getApartments(): Resource<List<ApartmentModel>> {
        return try {
            val apartmentModelList = arrayListOf<ApartmentModel>()
            val apartments: QuerySnapshot = database.collection(APARTMENT_COLLECTIONS).get().await()
            for (apartment in apartments) {
                apartmentModelList.add(apartment.toObject(ApartmentModel::class.java))
            }
            Log.e("kontrol", "viewmodel apartmanlar $apartmentModelList")
            Resource.Success(apartmentModelList)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Kullanıcının ait olduğu apartmanın bilgilerini getirir
    override suspend fun getAnApartment(): Resource<ApartmentModel> {
        return try {
            val apartmentDocumentId =
                sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)

            val apartmentDocument: DocumentSnapshot =
                database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).get()
                    .await()
            val apartmentModel = apartmentDocument.toObject(ApartmentModel::class.java)!!

            Resource.Success(apartmentModel)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

    //Uygulama içinde seçilen kullanıcının bilgilerini veritabanından getirir
    override suspend fun getAUserByNameAndDoorNumber(
        userName: String,
        doorNumber: String
    ): Resource<UserModel> {
        return try {
            val apartmentDocumentId =
                sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
            val users: QuerySnapshot? =
                database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
                    .collection(
                        USER_COLLECTION
                    ).whereEqualTo("doorNumber", doorNumber).whereEqualTo("fullName", userName)
                    .get().await()
            val user: DocumentSnapshot = users!!.documents[0]
            val userInfoModel = user.toObject(UserModel::class.java)!!
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
                "apartmentName" to apartmentName,
                "budget" to 0
            )
        ).await().id
        addNewUserToNewApartment(name, apartmentCode, carPlate, doorNumber, role, result)
    }

    override suspend fun changeUserDuesPaymentStatus(currentStatus: Boolean) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val userDocumentId = sharedRepository.readUserDocumentId(USER_DOCUMENT_ID)

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(
            USER_COLLECTION
        ).document(userDocumentId!!).update("duesPaymentStatus", currentStatus).await()
        val apartment =
            database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId).get().await()
        val apartmentModel = apartment.toObject(ApartmentModel::class.java)
        val apartmentDailyPaymentAmount: Double =
            apartmentModel?.monthlyPayment.toString().toDouble()
        val apartmentNewBudget: Double =
            apartmentDailyPaymentAmount + apartmentModel?.budget.toString().toDouble()

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId)
            .update("budget", apartmentNewBudget)

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
    override suspend fun writeUserDocumentIdToSharedPref(
        userName: String,
        apartmentCode: String
    ): String {
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

    override suspend fun addNewRequest(request: String, time: FieldValue) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val request = hashMapOf(
            "request" to request,
            "requestDate" to "",
            "time" to time
        )
        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
            .collection(RESIDENT_REQUESTS).add(request).await()
    }

    override suspend fun addNewManagerAnnouncement(announcement: String, time: FieldValue) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val announcement = hashMapOf(
            "announcement" to announcement,
            "time" to time
        )
        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
            .collection(MANAGER_ANNOUNCEMENT).add(announcement).await()
    }

    //yöneticinin yeni parasal olay girmesini sağlar
    override suspend fun addNewFinancialEvent(
        amount: Double,
        isExpense: Boolean,
        time: FieldValue,
        eventName: String
    ) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val apartment =
            database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).get().await()
        val apartmentModel = apartment.toObject(ApartmentModel::class.java)
        Log.e("kontrol", apartmentModel!!.apartmentName)
        var newBudget = 0.0
        newBudget = if (!isExpense) {
            apartmentModel.budget + amount

        } else {
            apartmentModel.budget - amount
        }

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId)
            .update("budget", newBudget)

        val newFinancialEvent = hashMapOf(
            "amount" to amount,
            "date" to time,
            "eventName" to eventName,
            "isExpense" to isExpense
        )
        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId)
            .collection(FINANCIAL_EVENTS).add(newFinancialEvent)
    }

    //Kullanıcın ait olduğu apartmana yeni mesaj eklenmesi
    override suspend fun sendNewMessageInChat(message: String, userName: String, time: FieldValue) {
        val message = hashMapOf(
            "userName" to userName,
            "message" to message,
            "time" to time
        )

        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
            .collection(CHAT_COLLECTION).add(
                message
            )
            .await()
    }

    //Kullanıcının ait olduğu apartmana ait mesajları canlı olarak getirilimesi
    override fun getChatMessages(): LiveData<Resource<List<ChatMessageModel>>> {

        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val liveData = MutableLiveData<Resource<List<ChatMessageModel>>>()
        liveData.value = Resource.Loading

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(
            CHAT_COLLECTION
        ).orderBy("time", Query.Direction.ASCENDING).addSnapshotListener { value, error ->
            if (error != null) {
                liveData.value = Resource.Failure(error)
            } else if (value != null) {
                val messages = mutableListOf<ChatMessageModel>()
                for (doc in value) {
                    val data = doc.toObject(ChatMessageModel::class.java)

                    messages.add(data)
                }
                liveData.value = Resource.Success(messages)
            }
        }
        return liveData
    }

    override suspend fun updateAnUser(
        userName: String,
        phoneNumber: String,
        carPlate: String,
        doorNumber: String
    ) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val userDocumentId = sharedRepository.readUserDocumentId(USER_DOCUMENT_ID)
        firebaseAuth.currentUser?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(userName).build())?.await()
        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).collection(
            USER_COLLECTION
        ).document(userDocumentId!!).update(
            "carPlate",
            carPlate,
            "userName",
            userName,
            "phoneNumber",
            phoneNumber,
            "doorNumber",
            doorNumber
        ).await()

    }

    //Yöneticinin apartman aidatının tutarını değiştirmesini sağlar
    override suspend fun setApartmentMonthlyPayment(amount: Double) {
        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(APARTMENT_DOCUMENT_ID)
        val apartment =
            database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!).get().await()

        database.collection(APARTMENT_COLLECTIONS).document(apartmentDocumentId!!)
            .update("monthlyPayment", amount)
    }

    //Apartman id sine ulaşmak için kullanılacak apartman adının shared preferencesten çekilmesi.
    private suspend fun reachToDocumentIdFromSharedPref(): String {
        val apartmentName = sharedRepository.readApartmentName(APARTMENT_NAME)
        return getApartmentDocumentId(apartmentName!!)
    }
}