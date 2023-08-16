package com.example.bluetoothtest


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.scosche.sdk24.RhythmDevice


class ScannedDeviceFragment : Fragment() {
    private var mListener: OnListFragmentInteractionListener? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ScannedDeviceRecyclerViewAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val data = sharedViewModel.age_data.value
        val view: View = inflater.inflate(R.layout.fragment_device_scanned, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.scanneddevices)
        recyclerView?.adapter = ScannedDeviceRecyclerViewAdapter(
            ArrayList<RhythmDevice>(), mListener, (activity as MainActivity?)?.getSdk()!!,
            (activity as MainActivity?)!!,
            (activity as MainActivity?)!!
        )
        adapter = recyclerView?.adapter as ScannedDeviceRecyclerViewAdapter
        return view
    }

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

    interface OnListFragmentInteractionListener

    fun handleBluetoothDevice(device: RhythmDevice?) {
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
}