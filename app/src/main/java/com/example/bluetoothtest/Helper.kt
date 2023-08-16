    package com.example.bluetoothtest

    import android.app.Application
    import androidx.lifecycle.ViewModelProvider

    class Helper: Application() {
        val sharedViewModel: SharedViewModel by lazy {
            ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(SharedViewModel::class.java)
        }


    }