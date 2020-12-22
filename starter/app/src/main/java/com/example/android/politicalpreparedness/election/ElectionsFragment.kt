package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.repository.ElectionRepository

class ElectionsFragment : Fragment() {

    private lateinit var viewModel: ElectionsViewModel
    private lateinit var viewModelFactory: ElectionsViewModelFactory
    private lateinit var repository: ElectionRepository
    private lateinit var binding: FragmentElectionBinding

    private lateinit var upcomingElectionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_election, container, false)

        repository = ElectionRepository(null)
        viewModelFactory = ElectionsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerViewAdapters()
        setupObservers()


        //TODO: Link elections to voter info


        return binding.root
    }

    private fun setupObservers() {
        viewModel.upcomingElections.observe(viewLifecycleOwner, { upcomingElections ->
            if (upcomingElections != null) {
                upcomingElectionsAdapter.submitList(upcomingElections)
            }
        })
        viewModel.savedElections.observe(viewLifecycleOwner, { elections ->
            if (elections != null) {
                val savedElections = elections.filter { election -> election.isSaved }
                savedElectionsAdapter.submitList(savedElections)
            }
        })
    }

    private fun setupRecyclerViewAdapters() {
        upcomingElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionSelected(election)
        })
        binding.upcomingElectionsRecyclerView.adapter = upcomingElectionsAdapter

        savedElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionSelected(election)
        })
        binding.savedElectionsRecyclerView.adapter = savedElectionsAdapter
    }

    //TODO: Refresh adapters when fragment loads

}