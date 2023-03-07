package com.leventsurer.manager.data.model


data class UserModel(
    val carPlate: String = "",
    val doorNumber:String = "",
    @field:JvmField  val duesPaymentStatus:Boolean = false,
    val fullName:String = "",
    val phoneNumber:String = "",
    val role:String = "",
    val imageLink:String = ""
)

