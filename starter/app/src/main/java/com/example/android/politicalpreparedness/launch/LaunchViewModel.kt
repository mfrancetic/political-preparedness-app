package com.example.android.politicalpreparedness.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.representative.DetailFragment

class LaunchViewModel : ViewModel() {

    private val _navigateToFragment = MutableLiveData<String>()
    val navigateToFragment: LiveData<String>
        get() = _navigateToFragment

    fun onUpcomingElectionsButtonClicked(){
        _navigateToFragment.value = ElectionsFragment::class.java.simpleName
    }

    fun onRepresentativeButtonClicked(){
        _navigateToFragment.value = DetailFragment::class.java.simpleName
    }

    fun navigateToFragmentDone() {
        _navigateToFragment.value = null
    }
}