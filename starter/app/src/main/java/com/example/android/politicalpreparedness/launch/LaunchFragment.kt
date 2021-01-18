package com.example.android.politicalpreparedness.launch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.election.ElectionsFragment
import com.example.android.politicalpreparedness.representative.DetailFragment

class LaunchFragment : Fragment() {

    private lateinit var binding: FragmentLaunchBinding
    private lateinit var viewModel: LaunchViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = FragmentLaunchBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(LaunchViewModel::class.java)

        binding.viewModel = viewModel

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.navigateToFragment.observe(viewLifecycleOwner, { string ->
            if (string == ElectionsFragment::class.java.simpleName) {
                navToElections()
                viewModel.navigateToFragmentDone()
            } else if (string == DetailFragment::class.java.simpleName) {
                navToRepresentatives()
                viewModel.navigateToFragmentDone()
            }
        })
    }

    private fun navToElections() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    private fun navToRepresentatives() {
        this.findNavController().navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }
}