package com.leventsurer.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val carPlate: String = "",
    val doorNumber:String = "",
    @field:JvmField  val duesPaymentStatus:Boolean = false,
    val fullName:String = "",
    val phoneNumber:String = "",
    val role:String = "",
    val imageLink:String = ""
) : Parcelable

