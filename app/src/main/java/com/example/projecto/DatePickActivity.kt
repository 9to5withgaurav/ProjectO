package com.example.projecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import androidx.core.os.bundleOf
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.remote.Datastore
import com.google.firebase.ktx.Firebase

class DatePickActivity : AppCompatActivity() {
    private lateinit var datePicker:DatePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_pick)
        val ctuBtn = findViewById<Button>(R.id.ctuBtn)
        var getPickupDate:String? = null
        datePicker = findViewById(R.id.datepicker)
        val getBundle = intent.extras
        val getPostId = getBundle?.getString("postId")
        // Example: Set initial date
        val initialYear = 2024
        val initialMonth = 6 // Months are zero-based (0 = January)
        val initialDay = 1
        datePicker.init(initialYear, initialMonth, initialDay) { view, year, monthOfYear, dayOfMonth ->
            // Handle date change event here
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            // Example: Print selected date
            getPickupDate = selectedDate
        }



        ctuBtn.setOnClickListener {
            val bundle = bundleOf()
            bundle.putStringArray("resultArray", arrayOf(getPostId,getPickupDate))
            val intent = Intent(this,CartActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}