package com.example.bluetoothtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.scosche.sdk24.ErrorType
import com.scosche.sdk24.FitFileContent.FitFileInfo
import com.scosche.sdk24.RhythmDevice
import com.scosche.sdk24.RhythmSDKDeviceCallback
import com.scosche.sdk24.RhythmSDKFitFileCallback
import com.scosche.sdk24.RhythmSDKScanningCallback
import com.scosche.sdk24.ScoscheSDK24


class MainActivity : AppCompatActivity(), RhythmSDKScanningCallback, RhythmSDKDeviceCallback, RhythmSDKFitFileCallback, RhythmPlusFragment.OnListFragmentInteractionListener {


    private lateinit var sdk: ScoscheSDK24
    private lateinit var fileName: String
    private lateinit var data: ByteArray
    private var isRhythm24: Boolean = false
    private val sharedViewModel: SharedViewModel
        get() = (application as Helper).sharedViewModel

    fun getSdk(): ScoscheSDK24? {
        return sdk
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.bluetoothtest.R.layout.activity_main)



        sdk = ScoscheSDK24(this)
        println("main")
        println(sharedViewModel.tmp)
        var fragment: Fragment? = null
        sharedViewModel.tmp.observe(this){
            println("cool")
            var xd: Fragment = RhythmPlusFragment::class.java.newInstance()
            supportFragmentManager.beginTransaction().replace(
                com.example.bluetoothtest.R.id.flContent,
                xd,
                "RhythmPlusFragment"
            ).addToBackStack(null).commit()
            println("HERE")
            sdk.startScan(this)

        }


        try {

                println("testtt")
                fragment = LoginFragment::class.java.newInstance()
                supportFragmentManager.beginTransaction().replace(
                    com.example.bluetoothtest.R.id.flContent,
                    fragment,
                    "LoginFragment"
                ).addToBackStack(null).commit()
                println("HERE")

            println("test")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun deviceFound(p0: RhythmDevice?) {
        println("found")
        val fragmentManager: FragmentManager = supportFragmentManager
        val f: Fragment? = fragmentManager.findFragmentByTag("RhythmPlusFragment")

        if (f != null && p0!!.getName() != null) {
            (f as RhythmPlusFragment).handleBluetoothDevice(p0)
        }
    }

    override fun error(errorType: ErrorType) {
        println("device connected")

        Toast.makeText(this, "Error scanning: $errorType", Toast.LENGTH_LONG).show()
        if (errorType == ErrorType.NO_LOCATION_PERMISSION) {
            ActivityCompat.requestPermissions(

                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )        }

    }

    override fun deviceLost(device: RhythmDevice?) {
        println("DEVICE LOST")
        val fragmentManager = supportFragmentManager
        val f = fragmentManager.findFragmentByTag("RhythmPlusFragment")
        if (f != null) {
            (f as RhythmPlusFragment).removeDevice(device!!)
        }
    }
    override fun deviceConnected(p0: RhythmDevice?) {
        runOnUiThread {
            println("DEVICE CONNECTED")
            val rhythmPlusFragment = RhythmPlusFragment()
            supportFragmentManager.beginTransaction()
                .replace(com.example.bluetoothtest.R.id.flContent, rhythmPlusFragment, "RhythmPlusFragment")
                .commit()
            isRhythm24 = false
        }
    }

    override fun updateHeartRate(heartRate: String?) {
        runOnUiThread {
            val fragmentManager = supportFragmentManager
            val f = fragmentManager.findFragmentByTag("RhythmPlusFragment")
            if (f != null) {
                (f as RhythmPlusFragment).updateHeartRate(heartRate)
            }
        }
    }


    override fun monitorStateInvalid() {
        runOnUiThread {
            val fragmentManager = supportFragmentManager
            //TODO: create method to get active fragment


            val f = fragmentManager.findFragmentByTag("RhythmPlusFragment")
            if (f != null) {
                (f as RhythmPlusFragment).updateHeartRate("???")
            }

        }
    }

    override fun updateBatteryLevel(batteryLevel: Int) {
        runOnUiThread {
            val fragmentManager = supportFragmentManager

            val f = fragmentManager.findFragmentByTag("RhythmPlusFragment")
            if (f != null) {
                (f as RhythmPlusFragment).updateBattery(batteryLevel)
            }

        }
    }

    override fun updateZone(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun updateSportMode(sportMode: Int) {

    }

    override fun updateFirmwareVersion(value: String?) {
        runOnUiThread {
            val fragmentManager = supportFragmentManager
            val f = fragmentManager.findFragmentByTag("RhythmPlusFragment")

        }
    }

    override fun fitFilesFound(files: List<FitFileInfo?>?) {

    }
    override fun fitFileDownloadComplete(data: ByteArray?, fileName: String?) {

    }
    override fun fitFileDeleteComplete(fileName: String) {

    }
    override fun downloadProgressUpdate(percent: Int, file: FitFileInfo?) {

    }

}
