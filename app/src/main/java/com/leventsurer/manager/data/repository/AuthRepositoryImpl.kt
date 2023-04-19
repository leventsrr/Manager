package com.leventsurer.manager.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.leventsurer.manager.data.model.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    //Kayıtlı kullanıcının girişiyapılır
    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            Resource.Success(result.user!!)

        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }

    }
    //Yenş kullanıcı kayıt işlemi yapılır
    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }
    //Çıkış işlemi yapılır
    override fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun deleteUser() {
        Log.e("kontrol","${firebaseAuth.currentUser}")
        //firebaseAuth.currentUser!!.delete().await()
        currentUser?.delete()?.await()
    }

    override fun updateUserPassword(newPassword: String) {
        currentUser!!.updatePassword(newPassword)
    }
}