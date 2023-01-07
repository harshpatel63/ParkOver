package com.example.parkover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.parkover.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import com.example.parkover.ar.ARFragment
import com.example.parkover.ar.HelloGeoRenderer
import com.example.parkover.examples.java.common.helpers.FullScreenHelper
import com.example.parkover.examples.java.common.samplerender.SampleRender
import com.example.parkover.map.MapFragment
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.example.parkover.helpers.ARCoreSessionLifecycleHelper
import com.example.parkover.helpers.GeoPermissionsHelper
import com.example.parkover.helpers.HelloGeoView
import com.google.ar.core.exceptions.*
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupTab()

    }

    private fun setupTab() {
        binding.bottomBar.setOnTabSelectListener(object :AnimatedBottomBar.OnTabSelectListener{
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when(newIndex){
                    0->replaceFragment(MapFragment())
                    1->replaceFragment(ARFragment())
                    else-> {

                    }
                }
            }

        })
        replaceFragment(MapFragment())
    }



    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

}