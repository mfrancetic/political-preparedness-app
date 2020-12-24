package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.*
import java.util.*

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

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
        getDummyData()
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

    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how
     * elections are saved/removed from the database.
     */
    fun followUnfollowElection() {
        val isSaved = voterInfo.value?.election?.isSaved
        if (isSaved != null) {
            voterInfo.value?.election?.isSaved = !isSaved
        }
    }
}