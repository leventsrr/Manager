package com.leventsurer.manager.data.repository

import android.net.Uri

interface FirebaseStorageRepository {

    suspend fun uploadImage(imageUri:Uri)
    suspend fun getUserImage()
    suspend fun getUsersImages()
}