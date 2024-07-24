package com.example.projecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ConfirmationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        val getBundle = intent.extras
        val getDate = getBundle?.getString("pickDate")
        val msg = findViewById<TextView>(R.id.msg)
        msg.text = resources.getString(R.string.confirmation_info,getDate)
        findViewById<Button>(R.id.homeBtn).setOnClickListener {
            try {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }
}