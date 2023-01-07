package com.example.parkover.map

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.parkover.MainActivity
import com.example.parkover.R
import com.example.parkover.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mfusedLocationClient: FusedLocationProviderClient
    private var mGoogleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)
        mfusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap?.let {
            it.mapType = GoogleMap.MAP_TYPE_NORMAL
            it.setMaxZoomPreference(21f)
            it.setOnMapClickListener { latlng ->
                (activity as MainActivity).lastMarker = MarkerOptions().position(latlng)
                (activity as MainActivity).lastMarker?.let { it2 ->
                    it.addMarker(it2)
                }
            }
            moveToCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun moveToCurrentLocation() {
        if(ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mfusedLocationClient.lastLocation.addOnCompleteListener {
                if(it.isSuccessful && it.result != null) {
                    moveCamera(LatLng(it.result.latitude, it.result.longitude))
                } else {
                    Toast.makeText(requireContext(), "Please enable location services to use the feature", Toast.LENGTH_LONG).show()
                }
            }
            mGoogleMap?.let {
                it.isMyLocationEnabled = true
                it.uiSettings.isMyLocationButtonEnabled = true
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    AlertDialog.Builder(context)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton(
                            "OK"
                        ) { _: DialogInterface?, _: Int ->
                            mPermissionResult.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                        }
                        .create()
                        .show()
                } else {
                    mPermissionResult.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }
    }

    private val mPermissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                moveToCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19F))
    }

}