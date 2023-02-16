package com.leventsurer.manager.data.repository

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(
    private val firebaseStorage : FirebaseStorage
) : FirebaseStorageRepository {

    @Inject
    lateinit var firebaseFireStore:FirebaseFirestore

    override suspend fun uploadImage(imageUri :Uri) {
       val storageRef = firebaseStorage.reference.child(System.currentTimeMillis().toString())
        imageUri.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if(task.isSuccessful){
                    storageRef.downloadUrl.addOnCompleteListener { uri ->

                        val map = HashMap<String,Any>()
                        map["images"] = uri.toString()
                        firebaseFireStore.collection("images").add(map).addOnCompleteListener { firestoreTask ->
                            if(firestoreTask.isSuccessful){
                               Log.e("kontrol","resim veritabanına yüklendi")
                            }else{
                                Log.e("kontrol","resim veritabanına yüklenmedi")
                            }
                        }
                    }
                }else{
                    Log.e("kontrol","yükleme hatası")
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