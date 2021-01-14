package com.example.android.politicalpreparedness.election

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Election

class VoterInfoFragment : Fragment() {

    private lateinit var binding: FragmentVoterInfoBinding
    private lateinit var viewModel: VoterInfoViewModel
    private lateinit var viewModelFactory: VoterInfoViewModelFactory
    private lateinit var dataSource: ElectionDao
    private lateinit var fragmentContext: Context
    val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_voter_info, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentContext = binding.address.context

        dataSource = ElectionDatabase.getInstance(fragmentContext).electionDao
        viewModelFactory = VoterInfoViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupObservers()

        viewModel.getVoterInfo(args)
    }

    private fun setupObservers() {
        viewModel.voterInfo.observe(viewLifecycleOwner, { voterInfo ->
            if (voterInfo != null) {
                val administrationBody = voterInfo.state?.get(0)?.electionAdministrationBody
                if (administrationBody != null) {
                    updateAdministrationBodyDetails(administrationBody)
                }
            }
        })

        viewModel.election.observe(viewLifecycleOwner, { election ->
            if (election != null) {
                updateFollowButton(election)
            }
        })

        viewModel.openWebUrl.observe(viewLifecycleOwner, { url ->
            if (!url.isNullOrBlank()) {
                openWebUrl(url)
                viewModel.openWebUrlDone()
            }
        })
    }

    private fun updateAdministrationBodyDetails(administrationBody: AdministrationBody) {
        if (!administrationBody.ballotInfoUrl.isNullOrBlank()) {
            binding.stateBallot.visibility = View.VISIBLE
        } else {
            binding.stateBallot.visibility = View.GONE
        }

        if (administrationBody.correspondenceAddress != null) {
            binding.stateCorrespondenceHeader.visibility = View.VISIBLE
        } else {
            binding.stateBallot.visibility = View.GONE
        }
    }

    private fun openWebUrl(url: String) {
        val openWebUrlIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        activity?.startActivity(openWebUrlIntent)
    }

    private fun updateFollowButton(election: Election) {
        var followElectionButtonText = getString(R.string.follow_election)
        if (election.isSaved) {
            followElectionButtonText = getString(R.string.unfollow_election)
        }
        binding.followElectionButton.text = followElectionButtonText
    }
}