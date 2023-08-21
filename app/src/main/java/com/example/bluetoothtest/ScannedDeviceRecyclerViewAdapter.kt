package com.example.bluetoothtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.ScoscheSDK24


class ScannedDeviceRecyclerViewAdapter(
    private val devices: MutableList<RhythmDevice>,
    private val mListener: RhythmPlusFragment.OnListFragmentInteractionListener ?,
    private val sdk: ScoscheSDK24,
    private val callback: RhythmSDKDeviceCallback,
    private val fitFileCallback: RhythmSDKFitFileCallback
) :
    RecyclerView.Adapter<ScannedDeviceRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.scanned_fragment, parent, false)
        println("in oncreateviewholder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mItem = devices[position]
        holder.mIdView.text = devices[position].getName()
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    fun addDevice(device: RhythmDevice?): Boolean {
        if (device != null) {
            for (i in devices.indices) {
                if (device.getName() == devices[i].getName()) {
                    return false
                }
            }
            println("NOT NULL")
            devices.add(device)
            return true
        }
        else{
            println("ADD DEVICE")
        }
        return false
    }

    fun getDevices(): List<RhythmDevice> {
        return devices
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(
        mView
    ) {
        val mIdView: TextView
        val mConnectView: TextView
        var mItem: RhythmDevice? = null
        init {
            println("init")

            mIdView = mView.findViewById(R.id.id)
            mConnectView = mView.findViewById(R.id.connect)
            mConnectView.setText("CONNECT")
            mView.isClickable = true
            mView.setOnClickListener {
                if (null != mListener && mItem != null) {
                    // fragment is attached to one) that an item has been selected.
                    sdk.connectDevice(mItem, callback, fitFileCallback)
                }
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + mConnectView.text + "'"
        }
    }
}
