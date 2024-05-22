package com.example.projecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

enum class Status{
    ACTIVATED,UNACTIVATED
}

enum class Times{
    Morning,Afternoon,Evening,Night
}