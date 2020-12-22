package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.repository.ElectionRepository
import java.lang.IllegalArgumentException

class ElectionsViewModelFactory(private val electionRepository: ElectionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(electionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}