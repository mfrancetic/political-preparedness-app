package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _administrationBody = MutableLiveData<AdministrationBody>()
    val administrationBody: LiveData<AdministrationBody>
        get() = _administrationBody

    private val _openWebUrl = MutableLiveData<String>()
    val openWebUrl: LiveData<String>
        get() = _openWebUrl

    init {
        _voterInfo.value = null
        _openWebUrl.value = null
        _administrationBody.value = null
    }

    fun getVoterInfo(args: VoterInfoFragmentArgs) {
        val electionId = args.argElectionId
        val division = args.argDivision
        viewModelScope.launch {
            dataSource.getElectionById(electionId).collect { election ->
                _election.value = election
            }
        }
    }

    private fun getDummyData() {
        val state = mutableListOf<State>()
        val address = Address("Amphitheatre Parkway", "1600 MountainView",
                "Mountain View", "California", "94043")
        val administrationBody = AdministrationBody("Administration body", "\"http://www.googleapis.com",
                "http://www.googleapis.com", "http://www.googleapis.com", address)
        state.add(State("Alabama", administrationBody));
        val addresses = listOf(address)
        val officials = mutableListOf<Official>()
        officials.add(Official("Mike Pence", addresses, "Democratic Party"))
        val election = Election(1, "Test 1", Date(), Division("1", "Division 1", "Division state 1"), true)
        _voterInfo.value = VoterInfoResponse(election, "http://www.googleapis.com", "", state.toList())
        _administrationBody.value = administrationBody
    }

    //TODO: Add var and methods to populate voter info

    fun openWebUrl(url: String) {
        _openWebUrl.value = url
    }

    fun openWebUrlDone() {
        _openWebUrl.value = null
    }

    //TODO: Add var and methods to save and remove elections to local database

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how
     * elections are saved/removed from the database.
     */
    fun followUnfollowElection() {
        if (election.value != null) {
            val isSaved: Boolean = election.value!!.isSaved
            election.value!!.isSaved = !isSaved
            viewModelScope.launch {
                updateElectionInDatabase()
            }
        }
    }

    private suspend fun updateElectionInDatabase() {
        withContext(Dispatchers.IO) {
            dataSource.insert(election.value!!)
        }
    }
}