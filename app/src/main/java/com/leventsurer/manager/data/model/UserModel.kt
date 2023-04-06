package com.leventsurer.manager.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    var carPlate: String = "",
    var doorNumber:String = "",
    @field:JvmField  val duesPaymentStatus:Boolean = false,
    var fullName:String = "",
    var phoneNumber:String = "",
    val role:String = "",
    val imageLink:String = ""
) : Parcelable

