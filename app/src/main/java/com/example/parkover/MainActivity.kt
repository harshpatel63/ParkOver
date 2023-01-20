package com.example.parkover

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.parkover.add.AddActivity
import com.example.parkover.ar.ARFragment
import com.example.parkover.databinding.ActivityMainBinding
import com.example.parkover.databinding.CarBikeDailogeBinding
import com.example.parkover.map.MapFragment
import com.google.firebase.database.*
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
//    private var mAuth: FirebaseAuth? = null

    private lateinit var binding: ActivityMainBinding
    var lastMarker: CoordinateModel? = null
    var listofCM : ArrayList<CoordinateModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // initializing all our variables.
        firebaseDatabase = FirebaseDatabase.getInstance()
//        mAuth = FirebaseAuth.getInstance()
        databaseReference = firebaseDatabase!!.getReference("Courses")

        setupTab()
        setupUI()
        getCoordinates()

    }

    private fun getCoordinates() {
        databaseReference!!.child("hacknitr63").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(CoordinateModel::class.java)?.let {
                            listofCM.add(it)
                            Log.i("harsh_debug", it.lat.toString() + it.lng.toString() + it.heading.toString())
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun setupUI() {
        binding.add.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
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
            binding.add.setOnClickListener {
                binding.add.animate().apply {
                    duration = 1000
                    rotationYBy(360f)
                }.withEndAction {
                    binding.add.animate().apply {
                        duration = 1000
                        rotationYBy(3600f)
                    }.start()
                }
            }
            //Animation End

        }
    }

    private fun setupTab() {
        binding.viewPager.isUserInputEnabled = false
        val adapter = HomeTabAdapter(this)
        binding.viewPager.adapter = adapter
        binding.bottomBar.setupWithViewPager2(binding.viewPager)
    }

}