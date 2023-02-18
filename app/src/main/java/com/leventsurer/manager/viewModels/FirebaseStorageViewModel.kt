package com.leventsurer.manager.viewModels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.leventsurer.manager.data.repository.FirebaseStorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FirebaseStorageViewModel @Inject constructor(
    private val storageRepository: FirebaseStorageRepository
) : ViewModel() {

    suspend fun uploadImage(imageUri: Uri) {
        storageRepository.uploadImage(imageUri)
    }

    suspend fun getUserImage() {
        storageRepository.getUserImage()
    }

    suspend fun getUsersImages() {
        storageRepository.getUsersImages()
    }

}