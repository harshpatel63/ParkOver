package com.example.parkover

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.parkover.ar.ARFragment
import com.example.parkover.databinding.ActivityMainBinding
import com.example.parkover.databinding.CarBikeDailogeBinding
import com.example.parkover.map.MapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.util.UUID

class MainActivity : AppCompatActivity() {

    var firebaseDatabase: FirebaseDatabase? = null
    var databaseReference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    var universalLat:Double = 23.1815
    var universalLng:Double = 79.9864

    private lateinit var binding: ActivityMainBinding
    var lastMarker: MarkerOptions? = null
    var latLng : LatLng= LatLng( universalLat, universalLng)
    lateinit var coordinateModelArrayList: ArrayList<CoordinateModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // initializing all our variables.
        firebaseDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        coordinateModelArrayList = ArrayList<CoordinateModel>()
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase!!.getReference("Courses")


//        setupTab()
        setupUI()
//        insertData("yo","me","yyo")
        insertData( 43.000000, -75.000000)
        getCoordinates()

    }

    private fun insertData(lat: Double, lng: Double) {
        val coordinateModel = CoordinateModel( lat, lng)
        val Uid = UUID.randomUUID().toString()
        // on below line we are calling a add value event
        // to pass data to firebase database.
        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // on below line we are setting data in our firebase database.
                databaseReference!!.child(Uid).setValue(coordinateModel)
                // displaying a toast message.
                Toast.makeText(applicationContext, "Coordinates Added..", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // displaying a failure message on below line.
                Toast.makeText(applicationContext, "Fail to add Course..", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun getCoordinates() {
        // on below line clearing our list.
//        courseRVModalArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // on below line we are hiding our progress bar.
//                loadingPB.setVisibility(View.GONE);
                // adding snapshot to our array list
                // on below line.
                Toast.makeText(applicationContext, "Coordinates Loaded in map", Toast.LENGTH_SHORT).show()
                snapshot.getValue(CoordinateModel::class.java)
                    ?.let {
                        coordinateModelArrayList.add(it)
                        universalLat = it.getLat()
                        universalLng = it.getLng()
                        latLng = LatLng( universalLat, universalLng)
                        setupTab()
                    }

                Log.i("MYTAG", "${coordinateModelArrayList.toString()} :'lng ' $universalLng,'lat ' $universalLat")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
//                loadingPB.setVisibility(View.GONE);
                Toast.makeText(applicationContext, "ChildChanged", Toast.LENGTH_SHORT).show()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // notifying our adapter when child is removed.
//                loadingPB.setVisibility(View.GONE);
                Toast.makeText(applicationContext, "ChildRemoved", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // notifying our adapter when child is moved.
//                loadingPB.setVisibility(View.GONE);
                Toast.makeText(applicationContext, "ChildMoved", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
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