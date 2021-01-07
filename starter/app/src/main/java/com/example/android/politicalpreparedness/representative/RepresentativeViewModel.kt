package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.network.models.*
import com.example.android.politicalpreparedness.representative.model.Representative

class RepresentativeViewModel : ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    init {
        _address.value = null
        _representatives.value = null
    }

    private fun getAddressDummyData(){
        _address.value = Address("Amphitheatre Parkway", "1600", "Mountain View",
                "California", "94043")
    }

    private fun getRepresentativeDummyData() {
        val representatives = mutableListOf<Representative>()
        val channels = mutableListOf<Channel>()
        val officials = mutableListOf<Int>()
        officials.add(1)
        channels.add(Channel("GooglePlus", "1"))
        channels.add(Channel("YouTube", "2"))
        channels.add(Channel("Facebook", "3"))
        channels.add(Channel("Twitter", "4"))
        val urls = mutableListOf<String>()
        urls.add("https://www.google.com")
        val official = Official("Donald J. Trump", null,
                "Democratic Party", null, urls, "", channels)
        representatives.add(Representative(official, Office("Office",
                Division("1", "USA", "Alabama"), officials)))
        representatives.add(Representative(official, Office("Office",
                Division("1", "USA", "Alabama"), officials)))
        _representatives.value = representatives
    }

    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //TODO: Create function get address from geo location
    fun onUseLocationClicked() {
        getAddressDummyData()
        getRepresentativeDummyData()
    }

    //TODO: Create function to get address from individual fields
    fun onFindMyRepresentativesClicked(address: Address?) {
        _address.value = address
        getRepresentativeDummyData()
    }
}