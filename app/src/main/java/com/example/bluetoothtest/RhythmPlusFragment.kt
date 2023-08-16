package com.example.bluetoothtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class RhythmPlusFragment : Fragment() {
    private var heartRateField: TextView? = null
    private var batteryField: TextView? = null


    private var firmwareVersionField: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(com.example.bluetoothtest.R.layout.fragment_rhthym_plus, container, false)
        val sharedview = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        var age = sharedview.age_data.value
        var height = sharedview.height_data.value
        var weight = sharedview.weight_data.value
        var finage = view?.findViewById<TextView>(R.id.txt_age)
        var finheight = view?.findViewById<TextView>(R.id.txt_height)
        var finweight = view?.findViewById<TextView>(R.id.txt_weight)
        var cal = view?.findViewById<TextView>(R.id.calories)
        heartRateField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.heart_rate)
        batteryField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.batteryLevelField)
        firmwareVersionField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.firmwareVersionField)

        finage?.text = age
        cal?.text = "0.0"
        finweight?.text = weight
        finheight?.text = height

        return view
    }

    fun updateHeartRate(heartRate: String?) {
        var total_calories:Double = 0.0
        val sharedview = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        var age = sharedview.age_data.value
        var height = sharedview.height_data.value
        var weight = sharedview.weight_data.value
        var cal = view?.findViewById<TextView>(R.id.calories)
        heartRateField!!.text = heartRate
        val bmr = (weight.toString().toDouble() * 10) + (6.25*height.toString().toDouble()) + (age.toString().toDouble()*5) + 5
        val adjustedbmr = bmr
        val caloriesmin = adjustedbmr/60
        val caloriessec = caloriesmin/60
        total_calories += bmr.toDouble()
        cal?.text = total_calories.toString()
    }

    fun updateBattery(batteryLevel: Int) {
        batteryField!!.text = batteryLevel.toString()
    }

    fun updateFirmwareVersion(value: String?) {
        firmwareVersionField!!.text = value
    }
}