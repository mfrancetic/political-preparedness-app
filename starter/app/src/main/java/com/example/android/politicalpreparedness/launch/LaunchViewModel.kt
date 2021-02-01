package com.example.android.politicalpreparedness.launch

import androidx.lifecycle.ViewModel
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.representative.DetailFragment
import com.example.android.politicalpreparedness.utils.SingleLiveEvent

class LaunchViewModel : ViewModel() {

    private val _navigateToFragment = SingleLiveEvent<String>()
    val navigateToFragment: SingleLiveEvent<String>
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