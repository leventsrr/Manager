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

}
