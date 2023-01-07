package com.example.parkover

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parkover.ar.ARFragment
import com.example.parkover.databinding.ActivityMainBinding
import com.example.parkover.databinding.CarBikeDailogeBinding
import com.example.parkover.map.MapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastMarker: MarkerOptions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupTab()
        setupUI()

    }

    private fun setupUI() {
        binding.carBike.setOnClickListener {
            val dailogeBinding = CarBikeDailogeBinding.inflate(layoutInflater)
            val myDailoge = Dialog(this)
            myDailoge.setContentView(dailogeBinding.root)

            myDailoge.setCancelable(true)
            myDailoge.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDailoge.show()

            dailogeBinding.carCard.setOnClickListener {
                binding.carBike.setImageResource(R.drawable.ic_car_24)
                myDailoge.dismiss()
                //Animation
                binding.carBike.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.start()
            }
            dailogeBinding.bikeCard.setOnClickListener {
                binding.carBike.setImageResource(R.drawable.ic_scooter)
                myDailoge.dismiss()
                //Animation
                binding.carBike.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.start()

            }

            //Animation start
            binding.nav.setOnClickListener {
                binding.nav.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.withEndAction {
                    binding.nav.animate().apply {
                        duration = 1000
                        rotationYBy(3600f)
                    }.start()
                }
            }
            //Animation End

        }
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