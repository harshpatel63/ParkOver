package com.example.parkover.ar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ARViewModel: ViewModel() {

    val _trackingState = MutableLiveData<String>()
    val trackingState: LiveData<String> = _trackingState

}