package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.ValidationTextWatcher
import com.example.android.politicalpreparedness.utils.areAllFieldsValid
import com.example.android.politicalpreparedness.utils.isValid
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        addTextChangedListeners()

        return binding.root
    }

    private fun addTextChangedListeners() {
        binding.addressLine1EditText.addTextChangedListener(ValidationTextWatcher(fragmentContext, binding.addressLine1EditText, binding.addressLine1))
        binding.cityEditText.addTextChangedListener(ValidationTextWatcher(fragmentContext, binding.cityEditText, binding.city))
        binding.zipEditText.addTextChangedListener(ValidationTextWatcher(fragmentContext, binding.zipEditText, binding.zip))
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

        viewModel.findRepresentativesButtonClicked.observe(viewLifecycleOwner, { address ->
            if (address != null) {
                hideKeyboard()
                if (areAllFieldsValid(fragmentContext, binding)) {
                    viewModel.setAddress(address)
                } else {
                    viewModel.setSnackbarMessage(fragmentContext.getString(R.string.all_fields_must_be_valid))
                }
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
        binding.representativesFragmentRecyclerView.adapter = adapter
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
                if (address != null) {
                    viewModel.getAddressFromGeolocation(address)
                    binding.address = address
                } else {
                    displaySnackbar(fragmentContext.getString(R.string.location_must_be_in_the_us))
                }
            }
        }
    }

    private fun geoCodeLocation(location: Location): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
        return addresses
                .map { address ->
                    if (address.countryCode == "US") {
                        Address(address.thoroughfare, address.subThoroughfare, address.locality,
                                address.adminArea, address.postalCode)
                    } else {
                        null
                    }
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onClear()
    }
}