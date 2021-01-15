package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepresentativesRepository {

    suspend fun getRepresentatives(address: Address): List<Representative> {
        var representatives = listOf<Representative>()
        withContext(Dispatchers.IO) {
            val representativeResponse = CivicsApi.retrofitService.getRepresentativesAsync(address.toFormattedString())
                    .await()
            val offices = representativeResponse.offices
            val officials = representativeResponse.officials
            representatives = offices.flatMap { office -> office.getRepresentatives(officials) }
        }
        return representatives
    }
}