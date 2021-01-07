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
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

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

        fragmentContext = binding.address.context

        dataSource = ElectionDatabase.getInstance(fragmentContext).electionDao
        viewModelFactory = VoterInfoViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupObservers()

        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        viewModel.getVoterInfo(args)

        //TODO: Handle loading of URLs

        //TODO: cont'd Handle save button clicks
        return binding.root
    }

    private fun setupObservers() {
        viewModel.voterInfo.observe(viewLifecycleOwner, { voterInfo ->
            if (voterInfo != null) {

            }
        })

        viewModel.election.observe(viewLifecycleOwner, { election ->
            if (election != null) {
                binding.election = election
                updateFollowButton(election)
            }
        })

        viewModel.openWebUrl.observe(viewLifecycleOwner, { url ->
            if (!url.isNullOrBlank()) {
                openWebUrl(url)
                viewModel.openWebUrlDone()
            }
        })
        viewModel.administrationBody.observe(viewLifecycleOwner, { administrationBody ->
            if (!administrationBody?.ballotInfoUrl.isNullOrBlank()) {
                binding.stateBallot.visibility = View.VISIBLE
            } else {
                binding.stateBallot.visibility = View.GONE
            }
            if (administrationBody?.correspondenceAddress != null) {
                binding.stateCorrespondenceHeader.visibility = View.VISIBLE
            } else {
                binding.stateBallot.visibility = View.GONE
            }
        })
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