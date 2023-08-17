package com.example.bluetoothtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


class RhythmPlusFragment : Fragment() {
    private var heartRateField: TextView? = null
    private var batteryField: TextView? = null
    var total_calories:Double = 0.0
    var adjustedbmr:Double = 0.0
    var count:Int = 0
    var bmr:Double = 0.0
    private val handler = Handler()

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
        handler.post(runnable_og)
        sharedview.tmp2.observe(viewLifecycleOwner){
            var total_result = heart_rate/60
            bmr = (weight.toString().toDouble() * 10) + (6.25*height.toString().toDouble()) + (age.toString().toDouble()*5) + 5
            adjustedbmr = bmr.toDouble()*(1+(total_result-50)/100)
            println(adjustedbmr)
            val caloriesmin = adjustedbmr/1440
            val caloriessec = caloriesmin/60
            total_calories += caloriessec.toDouble()
            println(caloriessec)
            cal?.text = total_calories.toString()
            heart_rate = 0.0
        }

        return view
    }

    fun updateHeartRate(heartRate: String?) {
        heartRateField!!.text = heartRate
    }
    private var heart_rate = 0.0
    private val runnable_og = object : Runnable {
        override fun run() {
            println("has run")
            val sharedview = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

            if(heartRateField?.text.toString() == "???"){
                heartRateField?.text = "70"
            }
            heart_rate += heartRateField?.text.toString().toDouble()
            println(heart_rate)


            count = count + 1
            if(count == 60){
                println(count)

                count = 0
                println(count)

                if(sharedview.tmp2.value == true){
                    sharedview.tmp2.value = false

                }
                else{
                    sharedview.tmp2.value = true

                }


            }
            handler.postDelayed(this, 1000) // Run again after 1 second
        }
    }

    fun updateBattery(batteryLevel: Int) {
        batteryField!!.text = batteryLevel.toString()
    }

    fun updateFirmwareVersion(value: String?) {
        firmwareVersionField!!.text = value
    }
}