package com.example.projecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

const val MOBILE = "mobile"

class LoginActivity : AppCompatActivity() {
    private var getMobileInput:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mobileInput = findViewById<EditText>(R.id.phoneText)
        val btn = findViewById<Button>(R.id.continueBtn)
        btn.setOnClickListener {
             getMobileInput = mobileInput.text.toString()
            when{
                getMobileInput!!.length < 10 || getMobileInput!!.length > 10-> {
                    mobileInput.setText("")
                    alertBox()
                }
                else -> {
                    val bundle = bundleOf(MOBILE to "$getMobileInput")
                    val intent = Intent(this, OtpActivity::class.java)
                    intent.putExtras(bundle)
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }

    private fun alertBox(){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Incorrect Mobile No.")
            .setTitle("Error!")
            .setPositiveButton("ok"
            ) { dialog, which ->
                dialog.cancel()
            }

        builder.create()
        builder.show()
    }

}