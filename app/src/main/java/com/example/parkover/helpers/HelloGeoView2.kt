/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.parkover.helpers

import android.content.Intent
import android.opengl.GLSurfaceView
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.parkover.CoordinateModel
import com.example.parkover.MainActivity
import com.example.parkover.R
import com.example.parkover.add.AddActivity
import com.example.parkover.examples.java.common.helpers.SnackbarHelper
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.ar.core.Earth
import com.google.ar.core.GeospatialPose
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

/** Contains UI elements for Hello Geo. */
class HelloGeoView2(val activity: AddActivity) : DefaultLifecycleObserver {
    var lastLatLng: CoordinateModel? = null
    val root = View.inflate(activity, R.layout.activity_add, null)
    val surfaceView = root.findViewById<GLSurfaceView>(R.id.surfaceview)

    val session
        get() = activity.arCoreSessionHelper.session

    val snackbarHelper = SnackbarHelper()

    var mapView: MapView2? = null
    val mapTouchWrapper = root.findViewById<MapTouchWrapper>(R.id.map_wrapper).apply {
        setup { screenLocation ->
            val latLng: LatLng =
                mapView?.googleMap?.projection?.fromScreenLocation(screenLocation) ?: return@setup
            activity.renderer.onMapClick(latLng)
        }
    }
    val mapFragment =
        (activity.supportFragmentManager.findFragmentById(R.id.map)!! as SupportMapFragment).also {
            it.getMapAsync { googleMap -> mapView = MapView2(activity, googleMap) }
        }

    val add = root.findViewById<TextView>(R.id.add_button).setOnClickListener {
        lastLatLng?.let {
            insertData(it.lat, it.lng, it.heading)
            Toast.makeText(activity.applicationContext, "Parking Spot Added..", Toast.LENGTH_SHORT).show()
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    val statusText = root.findViewById<TextView>(R.id.statusText)
    fun updateStatusText(earth: Earth, cameraGeospatialPose: GeospatialPose?) {
        activity.runOnUiThread {
            val poseText = if (cameraGeospatialPose == null) "" else
                activity.getString(R.string.geospatial_pose,
                    cameraGeospatialPose.latitude,
                    cameraGeospatialPose.longitude,
                    cameraGeospatialPose.horizontalAccuracy,
                    cameraGeospatialPose.altitude,
                    cameraGeospatialPose.verticalAccuracy,
                    cameraGeospatialPose.heading,
                    cameraGeospatialPose.headingAccuracy)
            cameraGeospatialPose?.let{
                    lastLatLng =
                        CoordinateModel(it.latitude, it.longitude, it.heading)
                }
            statusText.text = activity.resources.getString(R.string.earth_state,
                earth.earthState.toString(),
                earth.trackingState.toString(),
                poseText)
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        surfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        surfaceView.onPause()
    }

    private fun insertData(lat: Double, lng: Double, heading: Double) {
        val coordinateModel = CoordinateModel(lat, lng, heading)
        val Uid = UUID.randomUUID().toString()
        activity.databaseReference!!.child("hacknitr63").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                activity.databaseReference!!.child("hacknitr63").child(Uid).setValue(coordinateModel)
                Toast.makeText(activity.applicationContext, "Parking Spot Added..", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity.applicationContext, "Fail to add Parking spot..", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

}
