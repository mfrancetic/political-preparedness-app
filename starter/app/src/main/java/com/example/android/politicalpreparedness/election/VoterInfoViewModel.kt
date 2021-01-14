package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val electionRepository = ElectionRepository(database)

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    private val _openWebUrl = MutableLiveData<String>()
    val openWebUrl: LiveData<String>
        get() = _openWebUrl

    fun getVoterInfo(args: VoterInfoFragmentArgs) {
        val electionId = args.argElectionId
        val division = args.argDivision
        val address: String

        address = if (division.state.isNotEmpty()) {
            "${division.state} ${division.country}"
        } else {
            division.country
        }
        viewModelScope.launch {
            _voterInfo.value = electionRepository.getVoterInfo(electionId, address)
            database.electionDao.getElectionById(electionId).collect { election ->
                _election.value = election
            }
        }
    }

    fun openWebUrl(url: String) {
        _openWebUrl.value = url
    }

    fun openWebUrlDone() {
        _openWebUrl.value = null
    }

    fun followUnfollowElection() {
        if (_election.value != null) {
            val isSaved: Boolean = _election.value!!.isSaved
            _election.value?.isSaved = !isSaved
            viewModelScope.launch {
                updateElectionInDatabase()
            }
        }
    }

    private suspend fun updateElectionInDatabase() {
        withContext(Dispatchers.IO) {
            _election.value?.let { database.electionDao.insert(it) }
        }
    }
}