package com.leventsurer.manager.data.model

data class FinancialEventModel(
    val amount:String = "",
    val date:String = "",
    val eventName:String = "",
    @field:JvmField val isExpense:Boolean = false,
)

