package com.leventsurer.manager.data.model

data class ConciergeDutiesModel(
    val assignmentDate:String = "",
    val duty:String = "",
    @field:JvmField val isDone: Boolean = false
)

