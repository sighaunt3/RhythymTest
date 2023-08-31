package com.example.bluetoothtest

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.scosche.sdk24.RhythmDevice
import java.math.BigDecimal
import java.math.RoundingMode


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
        var caloriessec = 0.0
        var caloriesmin = 0.0
        var height = sharedview.height_data.value
        var weight = sharedview.weight_data.value
        val button = view.findViewById<Button>(R.id.button3)
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
            (activity as MainActivity?)!!,
            requireActivity().application
        )
        adapter = recyclerView?.adapter as ScannedDeviceRecyclerViewAdapter
        println("bart3111")
        val switch = view.findViewById<Switch>(R.id.switch2)
        val switch2 = view.findViewById<Switch>(R.id.switch666)
        val loader = view.findViewById<ProgressBar>(R.id.progressBar)
        val rec_view = view.findViewById<RecyclerView>(R.id.rech)
        val card2 = view.findViewById<CardView>(R.id.cardViewxd2)
        val text_card2 = view.findViewById<TextView>(R.id.textView2)
        text_card2.text = "Last Connected Device"

        var edit_test = view.findViewById<EditText>(R.id.editTextText)
        var edit_test2 = view.findViewById<EditText>(R.id.editTextText2)
        var edit_test3 = view.findViewById<EditText>(R.id.editTextText3)

        var but = view.findViewById<Button>(R.id.confirmbutton)
        switch2.setOnClickListener {
            val l = if(but.visibility == View.GONE) View.VISIBLE else View.GONE
            but.visibility = l
           val v = if(edit_test.visibility == View.GONE) View.VISIBLE else View.GONE
            edit_test.visibility = v
            val t = if(edit_test2.visibility == View.GONE) View.VISIBLE else View.GONE
            edit_test2.visibility = t
            val x = if(edit_test3.visibility == View.GONE) View.VISIBLE else View.GONE
            edit_test3.visibility = x

        }

        switch.setOnClickListener {
            val v = if(rec_view.visibility == View.GONE) View.VISIBLE else View.GONE
            rec_view.visibility = v
            val t = if(loader.visibility == View.GONE) View.VISIBLE else View.GONE
            loader.visibility = t
        }
        println(adapter!!.itemCount)
        view.findViewById<TextView>(R.id.card_txt).text = sharedview.tmp7.value

        val permission3 = android.Manifest.permission.POST_NOTIFICATIONS
        val requestCode3 = 2020 // A unique request code
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission3), requestCode3)
        heartRateField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.heart_rate)
        batteryField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.batterylevelfield)
        firmwareVersionField = view.findViewById<TextView>(com.example.bluetoothtest.R.id.firmwareVersionField)
        val img = view.findViewById<ImageView>(R.id.imageView9)

        sharedview.hr.value = 0.0
        cal?.text = "0.0"
        println("24")
            if(sharedview.tmp5.value == "YES") {
                println("aloha")
                val img2 = view.findViewById<ImageView>(R.id.imageView13)
                img2.setImageResource(R.drawable.icons8_circle_96___green)
                view.findViewById<TextView>(R.id.card_txt).text = sharedview.tmp7.value
                text_card2.text = "Currently Connected Device"
                val anim2: Animation = AlphaAnimation(0.0f, 1.0f)

                button.visibility = View.VISIBLE
                img.visibility = View.VISIBLE


                anim2.duration = 650 //You can manage the blinking time with this parameter

                anim2.startOffset = 20
                anim2.repeatMode = Animation.REVERSE
                anim2.repeatCount = Animation.INFINITE
                img2.startAnimation(anim2)

                requireActivity().startForegroundService(
                    Intent(
                        context,
                        BackgroundService::class.java
                    )
                )
                sharedview.tmp5.value = "NO"
            }




        button.setOnClickListener {
            button.visibility = View.INVISIBLE
            img.visibility = View.INVISIBLE
            view.findViewById<TextView>(R.id.card_txt).text = ""
            val prog = view.findViewById<ProgressBar>(R.id.progressBar2)
            prog.visibility = View.VISIBLE
            val img3 = view.findViewById<ImageView>(R.id.imageView2)
            img3.visibility = View.INVISIBLE
            text_card2.text = ""
            requireActivity().stopService(Intent(context, BackgroundService::class.java))

            val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (mBluetoothAdapter.isEnabled) {
                mBluetoothAdapter.disable()
                mBluetoothAdapter.enable()
                requireActivity().stopService(Intent(context, BackgroundService::class.java))

            }

            sharedview.tmp.value = true
            total_calories = 0.0
            heartRateField?.text = "---"
            batteryField?.text = "---"
            cal?.text = "0.0"
            val recyclerView = view.findViewById<RecyclerView>(R.id.rech)
            recyclerView?.adapter = ScannedDeviceRecyclerViewAdapter(
                ArrayList<RhythmDevice>(), mListener, (activity as MainActivity?)?.getSdk()!!,
                (activity as MainActivity?)!!,
                (activity as MainActivity?)!!,
                requireActivity().application
            )
            adapter = recyclerView?.adapter as ScannedDeviceRecyclerViewAdapter



        }

        sharedview.tmp2.observe(viewLifecycleOwner){
            println("UPDATED")
            println(sharedview.tmp2.value)
            if(sharedview.tmp3.value == false){
                var total_result = sharedview.hr.value!! /60.0
                bmr = (weight.toString().toDouble() * 10) + (6.25*height.toString().toDouble()) + (age.toString().toDouble()*5) + 5
                adjustedbmr = bmr.toDouble()*(1+(total_result-50)/100)
                caloriesmin = adjustedbmr/1440
                caloriessec = caloriesmin/60
                total_calories += caloriessec.toDouble()
                println(caloriessec)
                val bd = BigDecimal(total_calories)
                val rounded = bd.setScale(3, RoundingMode.HALF_UP)
                cal?.text = rounded.toDouble().toString()
                sharedview.tmp3.value = true
            }


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
                println("REMOVE DEVICE")
                val mutableDevicesList = adapter?.getDevices()?.toMutableList()
                mutableDevicesList?.remove(rhythmDevice)
                val recyclerView = view?.findViewById<RecyclerView>(R.id.rech)
                recyclerView?.adapter = ScannedDeviceRecyclerViewAdapter(
                    (mutableDevicesList ?: emptyList()) as MutableList<RhythmDevice>, mListener, (activity as MainActivity?)?.getSdk()!!,
                    (activity as MainActivity?)!!,
                    (activity as MainActivity?)!!,
                    requireActivity().application
                )
                adapter = recyclerView?.adapter as ScannedDeviceRecyclerViewAdapter
                println("NEW RECYCLER")
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