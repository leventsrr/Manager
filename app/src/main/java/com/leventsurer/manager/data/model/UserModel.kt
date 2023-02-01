package com.leventsurer.manager.data.model


data class UserModel(
    val carPlate: List<String>,
    val doorNumber:String,
    val duesPaymentStatus:Boolean,
    val fullName:String,
    val phoneNumber:String,
    val role:String,
)

