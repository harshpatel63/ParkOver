package com.example.parkover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parkover.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomBar: AnimatedBottomBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
        setContentView(R.layout.activity_main)
        bottomBar = findViewById(R.id.bottom_bar)
        replaceFragment(MapFragment())
        bottomBar.setOnTabSelectListener(object :AnimatedBottomBar.OnTabSelectListener{
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
    }
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()

    }
}