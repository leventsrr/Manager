package com.leventsurer.manager.data.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.leventsurer.manager.tools.constants.FirebaseConstants.APARTMENT_COLLECTIONS
import com.leventsurer.manager.tools.constants.FirebaseConstants.USER_COLLECTION
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.APARTMENT_DOCUMENT_ID
import com.leventsurer.manager.tools.constants.SharedPreferencesConstants.USER_DOCUMENT_ID
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(
    private val firebaseStorage: FirebaseStorage
) : FirebaseStorageRepository {

    @Inject
    lateinit var firebaseFireStore: FirebaseFirestore

    @Inject
    lateinit var sharedRepository: SharedRepositoryImpl

    //Storage a yeni fotoğraf eklenmesi
    override suspend fun uploadImage(imageUri: Uri) {
        val storageRef = firebaseStorage.reference.child(System.currentTimeMillis().toString())
        imageUri.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val map = HashMap<String, Any>()
                        map["images"] = uri.toString()
                        val apartmentDocumentId = sharedRepository.readApartmentDocumentId(
                            APARTMENT_DOCUMENT_ID
                        )
                        val userDocumentId = sharedRepository.readUserDocumentId(USER_DOCUMENT_ID)
                        firebaseFireStore.collection(APARTMENT_COLLECTIONS)
                            .document(apartmentDocumentId!!).collection(
                            USER_COLLECTION
                        ).document(userDocumentId!!).update("imageLink", uri.toString())

                        firebaseFireStore.collection("images").add(map)
                            .addOnCompleteListener { firestoreTask ->
                                if (firestoreTask.isSuccessful) {
                                    Log.e("kontrol", "resim veritabanına yüklendi")
                                } else {
                                    Log.e("kontrol", "resim veritabanına yüklenmedi")
                                }
                            }
                    }
                } else {
                    Log.e("kontrol", "yükleme hatası")
                }

            }
        }
    }

    override suspend fun getUserImage() {
        TODO("")
    }

    override suspend fun getUsersImages() {
        TODO("Not yet implemented")
    }
}