package com.example.bluetoothtest

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.scosche.sdk24.RhythmDevice


class RhythmPlusFragment : Fragment() {
    private var heartRateField: TextView? = null
    private var batteryField: TextView? = null
    var total_calories:Double = 0.0
    var adjustedbmr:Double = 0.0
    var count:Int = 0
    var bmr:Double = 0.0
    private val handler = Handler()
    private var mListener: OnListFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ScannedDeviceRecyclerViewAdapter? = null
    private var firmwareVersionField: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(com.example.bluetoothtest.R.layout.fragment_rhthym_plus, container, false)
        var age = sharedview.age_data.value
        var height = sharedview.height_data.value
        var weight = sharedview.weight_data.value

        var cal = view?.findViewById<TextView>(R.id.calories)
        val permission = android.Manifest.permission.BLUETOOTH_SCAN
        val requestCode = 123 // A unique request cod
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
        val permission2 = android.Manifest.permission.BLUETOOTH_CONNECT
        val requestCode2 = 2002 // A unique request code
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission2), requestCode2)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rech)
        recyclerView?.adapter = ScannedDeviceRecyclerViewAdapter(
            ArrayList<RhythmDevice>(), mListener, (activity as MainActivity?)?.getSdk()!!,
            (activity as MainActivity?)!!,
            (activity as MainActivity?)!!
        )
        adapter = recyclerView?.adapter as ScannedDeviceRecyclerViewAdapter
        println("bart3111")
        println(adapter!!.itemCount)
        val permission3 = android.Manifest.permission.POST_NOTIFICATIONS
        val requestCode3 = 2020 // A unique request code
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission3), requestCode3)
        heartRateField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.heart_rate)
        batteryField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.batteryLevelField)
        firmwareVersionField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.firmwareVersionField)
        sharedview.hr.value = 0.0
        cal?.text = "0.0"


        requireActivity().startForegroundService(
            Intent(
                context,
                BackgroundService::class.java
            )
        )

        sharedview.tmp2.observe(viewLifecycleOwner){
            var total_result = sharedview.hr.value!! /60.0
            bmr = (weight.toString().toDouble() * 10) + (6.25*height.toString().toDouble()) + (age.toString().toDouble()*5) + 5
            adjustedbmr = bmr.toDouble()*(1+(total_result-50)/100)
            println(adjustedbmr)
            val caloriesmin = adjustedbmr/1440
            val caloriessec = caloriesmin/60
            total_calories += caloriessec.toDouble()
            println(caloriessec)
            cal?.text = total_calories.toString()

        }

        return view
    }
    interface OnListFragmentInteractionListener

    override fun onAttach(context: Context) {

        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnListFragmentInteractionListener"
            )
        }
    }


    val devices: List<RhythmDevice>
        get() {
            adapter = (recyclerView!!.adapter as ScannedDeviceRecyclerViewAdapter?)
            return adapter?.getDevices()!!
        }


    @SuppressLint("NotifyDataSetChanged")
    fun handleBluetoothDevice(device: RhythmDevice?) {
        println("handle")
        if (adapter != null && device != null) {
            if (adapter?.addDevice(device) == true) {

                adapter?.notifyDataSetChanged()
            }
        }
    }

    fun removeDevice(device: RhythmDevice) {
        for (rhythmDevice: RhythmDevice in adapter?.getDevices()!!) {
            if ((rhythmDevice.getName() == device.getName())) {
                val mutableDevicesList = adapter?.getDevices()?.toMutableList()
                mutableDevicesList?.remove(rhythmDevice)

                val newAdapter = ScannedDeviceRecyclerViewAdapter(
                    (mutableDevicesList ?: emptyList()) as MutableList<RhythmDevice>, mListener,
                    (activity as MainActivity?)?.getSdk()!!,
                    (activity as MainActivity?)!!,
                    (activity as MainActivity?)!!
                )

                recyclerView?.adapter = newAdapter // Set the new adapter
            }
        }
    }

    companion object {
        private var adapter: ScannedDeviceRecyclerViewAdapter? = null
    }
    private val sharedview: SharedViewModel
        get() = (requireActivity().application as Helper).sharedViewModel
    fun updateHeartRate(heartRate: String?) {

        heartRateField!!.text = heartRate
        sharedview.heart_rate.value = heartRate
        println("in heart")
        println(sharedview.heart_rate.value)

    }



    fun updateBattery(batteryLevel: Int) {
        batteryField!!.text = batteryLevel.toString()
    }


}