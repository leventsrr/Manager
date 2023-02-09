package com.leventsurer.manager.data.repository
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import com.leventsurer.manager.data.model.Resource

interface AuthRepository {
    val currentUser : FirebaseUser?
    suspend fun login(email:String,password:String):Resource<FirebaseUser>
    suspend fun signup(name:String,email: String,password: String): Resource<FirebaseUser>
    fun logout()
}