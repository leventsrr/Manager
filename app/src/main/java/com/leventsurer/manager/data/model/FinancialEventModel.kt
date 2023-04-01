package com.leventsurer.manager.data.model

import com.google.firebase.firestore.FieldValue

data class FinancialEventModel(
    val amount:Double =0.0,
    val eventName:String = "",
    @field:JvmField val isExpense:Boolean = false,
)

