package com.example.projecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomappbar.BottomAppBar

const val KEY = "auth"
const val  MAIN = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val sharedPref = getSharedPreferences("userID",Context.MODE_PRIVATE)
        val fetch = sharedPref.getString(KEY,null)
        Log.i(MAIN,"$fetch")
        if (fetch != null){
            Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(this,LoginActivity::class.java)
            try {
                startActivity(intent)
            }catch (e:Exception){
               e.printStackTrace()
            }

        }

        val str = findViewById<TextView>(R.id.msg)
        str.text = resources.getStringArray(R.array.activityStatus)[0]

        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setOnMenuItemClickListener {menuItem->
            when(menuItem.itemId){
                R.id.help -> {
                    try {
                        val intent = Intent(this,HelpActivity::class.java)
                        startActivity(intent)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }

                    true
                }

                R.id.alert -> {

                    true
                }

                R.id.donate -> {

                    true
                }

                else -> false
            }
        }

    }
}

enum class Status{
    ACTIVATED,UNACTIVATED
}

enum class Times{
    Morning,Afternoon,Evening,Night
}

enum class Network{
    Available , Lost , Default
}

