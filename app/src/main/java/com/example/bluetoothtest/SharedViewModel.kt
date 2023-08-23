package com.example.bluetoothtest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scosche.sdk24.ScoscheSDK24

class SharedViewModel: ViewModel() {
    val age_data: MutableLiveData<String> = MutableLiveData()
    val height_data: MutableLiveData<String> = MutableLiveData()
    val hr: MutableLiveData<Double> = MutableLiveData()
    val weight_data: MutableLiveData<String> = MutableLiveData()
    var tmp:MutableLiveData<Boolean> = MutableLiveData()
    var tmp2:MutableLiveData<Boolean> = MutableLiveData()
    val heart_rate: MutableLiveData<String> = MutableLiveData()
    lateinit var tmp_sdk: ScoscheSDK24
    var tmp3:MutableLiveData<Boolean> = MutableLiveData()
    var tmp4:MutableLiveData<Boolean> = MutableLiveData()
    var tmp5:MutableLiveData<String> = MutableLiveData()
    var tmp7:MutableLiveData<String> = MutableLiveData()



}