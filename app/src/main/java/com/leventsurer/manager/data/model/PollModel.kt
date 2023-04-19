package com.leventsurer.manager.data.model

import kotlin.collections.ArrayList

data class PollModel(
    var agreeCount:Int = 0,
    var disagreeCount:Int = 0,
    val people: Map<String, ArrayList<String>> = mapOf("agreePeople" to arrayListOf(),"disagreePeople" to arrayListOf()),
    val pollText:String = "",

)
