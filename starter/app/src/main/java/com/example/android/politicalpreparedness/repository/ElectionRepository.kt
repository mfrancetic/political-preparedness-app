package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val electionDatabase: ElectionDatabase) {

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            var elections: List<Election>
            withContext(Dispatchers.IO) {
                val electionResponse: ElectionResponse = CivicsApi.retrofitService.getElectionsAsync()
                        .await()
                elections = electionResponse.elections
                electionDatabase.electionDao.insertAll(elections)
            }
        }
    }
}