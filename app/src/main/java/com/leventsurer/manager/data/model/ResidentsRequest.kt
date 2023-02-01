package com.leventsurer.manager.data.model

import java.util.Date

data class ResidentsRequest(
    val isReviewed:Boolean,
    val request:String,
    val requestTime:Date,
    val residentDoorNumber:String,
    val residentsName:String,
)

