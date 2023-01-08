package com.example.parkover.ar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.parkover.MainActivity
import com.example.parkover.examples.java.common.samplerender.SampleRender
import com.example.parkover.helpers.ARCoreSessionLifecycleHelper
import com.example.parkover.helpers.GeoPermissionsHelper
import com.example.parkover.helpers.HelloGeoView
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.core.exceptions.*


class ARFragment : Fragment() {
    companion object {
        private const val TAG = "ARFragment"
    }

    lateinit var arCoreSessionHelper: ARCoreSessionLifecycleHelper
    lateinit var view: HelloGeoView
    lateinit var renderer: HelloGeoRenderer

    lateinit var viewModel: ARViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return setupAr()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ARViewModel::class.java)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.trackingState.observe(viewLifecycleOwner) {
            timePass()
        }
    }

    private fun timePass() {
        (activity as MainActivity).lastMarker?.let {
            renderer.onMapClick(it)
        }
    }

    private fun setupAr(): View? {
        arCoreSessionHelper = ARCoreSessionLifecycleHelper(this.requireActivity())
        arCoreSessionHelper.exceptionCallback =
            { exception ->
                val message =
                    when (exception) {
                        is UnavailableUserDeclinedInstallationException ->
                            "Please install Google Play Services for AR"
                        is UnavailableApkTooOldException -> "Please update ARCore"
                        is UnavailableSdkTooOldException -> "Please update this app"
                        is UnavailableDeviceNotCompatibleException -> "This device does not support AR"
                        is CameraNotAvailableException -> "Camera not available. Try restarting the app."
                        else -> "Failed to create AR session: $exception"
                    }
                Log.e(TAG, "ARCore threw an exception", exception)
                view.snackbarHelper.showError(activity, message)
            }

        arCoreSessionHelper.beforeSessionResume = ::configureSession
        lifecycle.addObserver(arCoreSessionHelper)

        // Set up the Hello AR renderer.
        renderer = HelloGeoRenderer(this)
        lifecycle.addObserver(renderer)

        // Set up Hello AR UI.
        view = HelloGeoView(this)
        lifecycle.addObserver(view)

        // Sets up an example renderer using our HelloGeoRenderer.
        SampleRender(view.surfaceView, renderer, this.requireActivity().assets)

        return view.root
    }

    fun configureSession(session: Session) {
        session.configure(
            session.config.apply {
                geospatialMode = Config.GeospatialMode.ENABLED
            }
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        if (!GeoPermissionsHelper.hasGeoPermissions(this.requireActivity())) {
            // Use toast instead of snackbar here since the activity will exit.
            Toast.makeText(requireContext(), "Camera and location permissions are needed to run this application", Toast.LENGTH_LONG)
                .show()
            if (!GeoPermissionsHelper.shouldShowRequestPermissionRationale(this.requireActivity())) {
                // Permission denied with checking "Do not ask again".
                GeoPermissionsHelper.launchPermissionSettings(this.requireActivity())
            }
            this.requireActivity().finish()
        }
    }

}