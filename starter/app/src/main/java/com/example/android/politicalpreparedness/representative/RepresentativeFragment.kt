package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        const val LOCATION_REQUEST_PERMISSION = 2
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
        binding.lifecycleOwner = this
        binding.address = Address("", "", "", "Alabama", "")

        fragmentContext = binding.addressLine1.context

        setupRecyclerViewAdapter()
        setupObservers()
        setupStateSpinner()

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
                hideKeyboard()
                adapter.submitList(representatives)
            }
        })

        viewModel.snackbarMessage.observe(viewLifecycleOwner, { message ->
            if (!message.isNullOrBlank()) {
                displaySnackbar(message)
                viewModel.snackbarMessageDone()
            }
        })

        viewModel.locationButtonClicked.observe(viewLifecycleOwner, { buttonClicked ->
            if (buttonClicked) {
                hideKeyboard()
                if (areLocationServicesEnabled(fragmentContext)) {
                    if (checkLocationPermissions()) {
                        getLocation()
                    }
                } else {
                    promptUserToEnableLocationServices()
                }

                viewModel.locationRetrieved()
            }
        })
    }

    private fun areLocationServicesEnabled(fragmentContext: Context): Boolean {
        val locationManager =
                fragmentContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun displaySnackbar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
    }

    private fun promptUserToEnableLocationServices() {
        viewModel.setSnackbarMessage(fragmentContext.getString(R.string.location_services_disabled_snackbar))
    }

    private fun setupRecyclerViewAdapter() {
        adapter = RepresentativeListAdapter()
        binding.fragmentRepresentativesRecyclerView.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                if ((areLocationServicesEnabled(fragmentContext))) {
                    promptUserToGrantLocationPermission()
                } else {
                    promptUserToEnableLocationServices()
                }
            }
        }
    }

    private fun promptUserToGrantLocationPermission() {
        viewModel.setSnackbarMessage(fragmentContext.getString(R.string.location_permission_not_granted))
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_PERMISSION)
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                fragmentContext,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (isPermissionGranted()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                val address = location?.let { geoCodeLocation(it) }
                viewModel.getAddressFromGeolocation(address)
                binding.address = address
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality,
                            address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}