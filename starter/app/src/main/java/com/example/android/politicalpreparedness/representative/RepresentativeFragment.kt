package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListener
import java.util.Locale

class DetailFragment : Fragment() {


    companion object {
        //TODO: Add Constant for Location request
    }

    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var adapter: RepresentativeListAdapter
    private lateinit var fragmentContext: Context

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        binding.viewModel = viewModel
        binding.address = Address("", "", "", "Alabama", "")
        binding.lifecycleOwner = this

        fragmentContext = binding.addressLine1.context

        setupRecyclerViewAdapter()
        setupObservers()
        setupStateSpinner()

        //TODO: Establish button listeners for field and location search
        return binding.root
    }

    private fun setupStateSpinner() {
        val spinner = binding.state
        ArrayAdapter.createFromResource(fragmentContext, R.array.states, android.R.layout.simple_spinner_dropdown_item)
                .also { adapter ->
                    spinner.adapter = adapter
                }
    }

    private fun setupObservers() {
        viewModel.representatives.observe(viewLifecycleOwner, { representatives ->
            if (!representatives.isNullOrEmpty()) {
                adapter.submitList(representatives)
            }
        })
    }

    private fun setupRecyclerViewAdapter() {
        adapter = RepresentativeListAdapter()
        binding.fragmentRepresentativesRecyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}