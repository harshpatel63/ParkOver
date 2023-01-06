package com.example.parkover

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkover.databinding.ActivityMainBinding
import com.example.parkover.map.MapFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initViews()
    }
    private fun initViews() {
        val mapFragment = MapFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, mapFragment)
            .commit()
    }
}