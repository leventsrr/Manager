package com.leventsurer.manager.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.leventsurer.manager.data.model.ConciergeAnnouncementModel
import com.leventsurer.manager.data.model.Resource
import com.leventsurer.manager.data.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DatabaseViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
): ViewModel(){

    private val _conciergeAnnouncementFlow = MutableStateFlow<Resource<ArrayList<ConciergeAnnouncementModel>>?>(null)
    val conciergeAnnouncementFlow : StateFlow<Resource<ArrayList<ConciergeAnnouncementModel>>?> = _conciergeAnnouncementFlow


    fun getConciergeAnnouncement()= viewModelScope.launch {
        _conciergeAnnouncementFlow.value = Resource.Loading
        val result = databaseRepository.getConciergeAnnouncements()
        _conciergeAnnouncementFlow.value = result
        Log.e("cevap",result.toString())
    }


}