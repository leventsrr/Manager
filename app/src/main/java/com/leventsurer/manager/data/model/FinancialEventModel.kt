package com.leventsurer.manager.data.model

import java.util.Date

data class FinancialEventModel(
    val amount:Double,
    val date:Date,
    val isExpense:Boolean,
    val name:String,
)

