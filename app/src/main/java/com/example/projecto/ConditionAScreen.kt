package com.example.projecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class ConditionAScreen : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_condition_a_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btn = view.findViewById<Button>(R.id.nxt)
        btn.setOnClickListener {
            findNavController().navigate(R.id.action_conditionAScreen_to_conditionBScreen)
        }
    }


}