package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class ElectionsViewModel(private val electionRepository: ElectionRepository) : ViewModel() {

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
                getUpcomingElections()
            } catch (e: Exception) {
                println("Exception getting upcoming elections from API: $e.message")
            }
        }
        _savedElections.value = getDummyElections()
        _navigateToVoterInfo.value = null
    }

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private suspend fun getUpcomingElections() {
        var elections: List<Election>
        withContext(Dispatchers.IO) {
            val electionResponse: ElectionResponse = CivicsApi.retrofitService.getElectionsAsync()
                    .await()
            elections = electionResponse.elections
        }
        _upcomingElections.value = elections
    }

    private fun getDummyElections(): List<Election> {
        val election1 = Election(1, "Test 1", Date(), Division("1", "Division 1", "Division state 1"), true)
        val election2 = Election(2, "Test 2", Date(), Division("2", "Division 2", "Division state 2"), false)
        val election3 = Election(1, "Test 3", Date(), Division("3", "Division 3", "Division state 3"), true)
        val election4 = Election(2, "Test 4", Date(), Division("4", "Division 4", "Division state 4"), false)
        return listOf(election1, election2, election3, election4)
    }

    fun onElectionSelected(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun onElectionSelectedDone() {
        _navigateToVoterInfo.value = null
    }
}