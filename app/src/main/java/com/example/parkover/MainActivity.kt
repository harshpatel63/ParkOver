package com.example.parkover

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
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

        val carDailoge = findViewById<ImageView>(R.id.car_bike)
        carDailoge.setOnClickListener {
            val dailogeBinding = layoutInflater.inflate(R.layout.car_bike_dailoge,null)
            val myDailoge = Dialog(this)
            myDailoge.setContentView(dailogeBinding)

            myDailoge.setCancelable(true)
            myDailoge.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDailoge.show()

            val carCard = dailogeBinding.findViewById<CardView>(R.id.car_card)
            carCard.setOnClickListener {
                carDailoge.setImageResource(R.drawable.ic_car_24)
                myDailoge.dismiss()
                //Animation
                carDailoge.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.start()
            }
            val bikeCard = dailogeBinding.findViewById<CardView>(R.id.bike_card)
            bikeCard.setOnClickListener {
                carDailoge.setImageResource(R.drawable.ic_scooter)
                myDailoge.dismiss()
                //Animation
                carDailoge.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.start()

            }

            //Animation start
            val navImage = findViewById<ImageView>(R.id.nav)
            navImage.setOnClickListener {
                navImage.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.withEndAction {
                    navImage.animate().apply {
                        duration = 1000
                        rotationYBy(3600f)
                    }.start()
                }
            }
            //Animation End
        }
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