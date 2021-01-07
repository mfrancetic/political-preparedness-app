package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ElectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val electionRepository = ElectionRepository(database)

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
        get() = _navigateToVoterInfo

    init {
        viewModelScope.launch {
            try {
                electionRepository.refreshElections()
            } catch (e: Exception) {
                println("Exception getting upcoming elections from API: $e.message")
            }
        }
        getUpcomingElections()
        getSavedElections()
        _navigateToVoterInfo.value = null
    }

    private fun getUpcomingElections() {
        viewModelScope.launch {
            database.electionDao.getUpcomingElections().collect { elections ->
                _upcomingElections.value = elections
            }
        }
    }

    private fun getSavedElections() {
        viewModelScope.launch {
            database.electionDao.getSavedElections().collect { elections ->
                _savedElections.value = elections
            }
        }
    }

    fun onElectionSelected(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun onElectionSelectedDone() {
        _navigateToVoterInfo.value = null
    }
}