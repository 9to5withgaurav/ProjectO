package com.example.projecto

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

const val MOBILE = "mobile"

class LoginActivity : AppCompatActivity() {
    private lateinit var btn: Button
    private var getMobileInput: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var status:String? = null
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            status = "Internet Lost ! Please check your connection"
            updateNetworkStatus(status)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mobileInput = findViewById<EditText>(R.id.phoneInput)
        btn = findViewById<Button>(R.id.continueBtn)
        btn.setOnClickListener {
            getMobileInput = mobileInput.text.toString()
            when {
                getMobileInput!!.length < 10 -> {
                    mobileInput.setText("")
                    alertBox("Incorrect Mobile No.")
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

       //for Network Status
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val connectivityManager =
            getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)

    }

    private fun alertBox(msg: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(msg)
            .setTitle("Error!")
            .setPositiveButton(
                "ok"
            ) { dialog, which ->
                dialog.cancel()
            }

        builder.create()
        builder.show()
    }

    private fun updateNetworkStatus(msg: String?) {
        handler.post(object : Runnable {
            override fun run() {
                alertBox(msg)
            }

        })
    }
}
