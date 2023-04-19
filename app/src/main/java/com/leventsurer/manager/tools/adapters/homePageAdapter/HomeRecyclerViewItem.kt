package com.leventsurer.manager.tools.adapters.homePageAdapter

sealed class HomeRecyclerViewItem{

    class ManagerAnnouncement(
        val announcement:String
    ):HomeRecyclerViewItem()

    class ConciergeAnnouncement(
        val announcement:String
    ):HomeRecyclerViewItem()

    class ResidentRequest(
        val request:String
    ):HomeRecyclerViewItem()

    class Polls(
        val pollText:String,
        val agreeCount:Int,
        val disagreeCount:Int,
        val people: Map<String, ArrayList<String>> = mapOf("agreePeople" to arrayListOf(),"disagreePeople" to arrayListOf()),
    ):HomeRecyclerViewItem()

}
