package com.example.bluetoothtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val age_data: MutableLiveData<String> = MutableLiveData()
    val height_data: MutableLiveData<String> = MutableLiveData()
    val hr: MutableLiveData<Double> = MutableLiveData()
    val weight_data: MutableLiveData<String> = MutableLiveData()
    var tmp:MutableLiveData<Boolean> = MutableLiveData()
    var tmp2:MutableLiveData<Boolean> = MutableLiveData()
    val heart_rate: MutableLiveData<String> = MutableLiveData()



}