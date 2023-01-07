package com.example.parkover

import java.util.*

class CoordinateModel {
    private var lat = 0.0
    private var lng = 0.0
    private val heading = 0.0
//    private var Uid = UUID.randomUUID().toString()
//    fun getUid(): String = Uid
//
//    fun setUid(uid: String) {
//        this.Uid = uid
//    }

    // creating an empty constructor.
    constructor() {}

    fun setLat(lat: Double) {
        this.lat = lat
    }
    fun getLat(): Double = lat

    fun getLng(): Double = lng

    fun setLng(lng: Double) {
        this.lng = lng
    }

    constructor(lat: Double, lng: Double) {
        this.lat = lat
//        Uid = Uid
        this.lng = lng
    }
}