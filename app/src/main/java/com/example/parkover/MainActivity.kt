package com.example.parkover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parkover.ar.ARFragment
import com.example.parkover.databinding.ActivityMainBinding
import com.example.parkover.map.MapFragment
import com.google.android.gms.maps.model.MarkerOptions
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var lastMarker: MarkerOptions? = null

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