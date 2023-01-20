package com.example.parkover

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.parkover.ar.ARFragment

class HomeTabAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> {
                val fragment = com.example.parkover.map.MapFragment()
                fragment
            }
            1 -> ARFragment()
            else -> {
                com.example.parkover.map.MapFragment()
            }
        }
    }
}