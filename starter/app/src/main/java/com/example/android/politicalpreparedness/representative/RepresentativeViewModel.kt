package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativesRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.ERROR_NO_DATA_FOUND
import com.example.android.politicalpreparedness.utils.SingleLiveEvent
import com.example.android.politicalpreparedness.utils.isValid
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel : ViewModel() {

    private val representativeRepository = RepresentativesRepository()

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _locationButtonClicked = SingleLiveEvent<Boolean>()
    val locationButtonClicked: SingleLiveEvent<Boolean>
        get() = _locationButtonClicked

    private val _snackbarMessage = SingleLiveEvent<String>()
    val snackbarMessage: SingleLiveEvent<String>
        get() = _snackbarMessage

    private val _findRepresentativesButtonClicked = SingleLiveEvent<Address>()
    val findRepresentativesButtonClicked: SingleLiveEvent<Address>
        get() = _findRepresentativesButtonClicked

    private val _isRepresentativeDataLoading = SingleLiveEvent<Boolean>()
    val isRepresentativeDataLoading: SingleLiveEvent<Boolean>
    get() = _isRepresentativeDataLoading

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

    private fun getRepresentativesIfAddressValid(address: Address) {
        _address.value = address
        if (address.isValid()) {
            setRepresentativeDataLoading(true)
            viewModelScope.launch {
                getRepresentatives(address)
            }
        }
    }

    private fun setRepresentativeDataLoading(isDataLoading: Boolean) {
        _isRepresentativeDataLoading.value = isDataLoading
    }

    private suspend fun getRepresentatives(address: Address) {
        try {
            _representatives.value = representativeRepository.getRepresentatives(address)
        } catch (e: Exception) {
            _representatives.value = null
            _snackbarMessage.value = ERROR_NO_DATA_FOUND
        }

        setRepresentativeDataLoading(false)
    }
}