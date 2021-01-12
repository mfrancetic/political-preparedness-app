package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.isValid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepresentativeViewModel : ViewModel() {

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _locationButtonClicked = MutableLiveData<Boolean>()
    val locationButtonClicked: LiveData<Boolean>
        get() = _locationButtonClicked

    private val _snackbarMessage = MutableLiveData<String>()
    val snackbarMessage: LiveData<String>
        get() = _snackbarMessage

    private val _findRepresentativesButtonClicked = MutableLiveData<Address>()
    val findRepresentativesButtonClicked: LiveData<Address>
    get() = _findRepresentativesButtonClicked

    init {
        _address.value = null
        _representatives.value = null
        _locationButtonClicked.value = false
        _snackbarMessage.value = null
        _findRepresentativesButtonClicked.value = null
    }

    fun onUseLocationClicked() {
        _locationButtonClicked.value = true
    }

    fun locationRetrieved() {
        _locationButtonClicked.value = false
    }

    fun getAddressFromGeolocation(address: Address?) {
        if (address != null) {
            getRepresentativesIfAddressValid(address)
        }
    }

    fun onFindMyRepresentativesClicked(address: Address) {
        _findRepresentativesButtonClicked.value = address
    }

    fun setAddress(address: Address) {
        _address.value = address
        getRepresentativesIfAddressValid(address)
    }

    fun setSnackbarMessage(message: String) {
        _snackbarMessage.value = message
    }

    fun snackbarMessageDone() {
        _snackbarMessage.value = null
    }

    private fun getRepresentativesIfAddressValid(address: Address) {
        _address.value = address
        if (address.isValid()) {
            viewModelScope.launch {
                getRepresentatives(address)
            }
        }
    }

    private suspend fun getRepresentatives(address: Address) {
        var representatives = listOf<Representative>()
        withContext(Dispatchers.IO) {
            val representativeResponse = CivicsApi.retrofitService.getRepresentativesAsync(address.toFormattedString())
                    .await()
            val offices = representativeResponse.offices
            val officials = representativeResponse.officials
            representatives = offices.flatMap { office -> office.getRepresentatives(officials) }
        }
        _representatives.value = representatives
    }
}