package com.example.bluetoothtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment


class LoginFragment: Fragment() {
    private val sharedViewModel: SharedViewModel
        get() = (requireActivity().application as Helper).sharedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val view: View = inflater.inflate(R.layout.fragment_login, container, false)
        val age = view.findViewById<EditText>(R.id.age_txt)
        val height = view.findViewById<EditText>(R.id.height_txt)
        val weight = view.findViewById<EditText>(R.id.weight_txt)
        val button = view.findViewById<Button>(R.id.button)
        button.setOnClickListener{
            sharedViewModel.age_data.value = age.text.toString()
            sharedViewModel.height_data.value = height.text.toString()
            sharedViewModel.weight_data.value = weight.text.toString()
            sharedViewModel.tmp.value = true
            println(sharedViewModel.tmp)
            requireActivity().supportFragmentManager
                .beginTransaction()
                .remove(this) // Remove the current fragment
                .commit()




        }

        return view
    }

}