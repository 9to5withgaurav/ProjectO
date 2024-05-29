package com.example.projecto

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.chaos.view.PinView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

const val TAG = "OtpLogin"
const val STATE = "Progress State"

class OtpActivity : AppCompatActivity() {
    var verificationId:String? = null
    var auth: FirebaseAuth? = null
    private lateinit var resendBtn : TextView
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        var getMobile = intent.getStringExtra(MOBILE)
        getMobile = "+1$getMobile"


        val pinInput = findViewById<PinView>(R.id.pin)
        auth = FirebaseAuth.getInstance()
        val snackBarViw = findViewById<CoordinatorLayout>(R.id.coordinateView)
        val verifyBtn = findViewById<Button>(R.id.verify_btn)
         resendBtn = findViewById(R.id.resendCode)

        val subtitle = findViewById<TextView>(R.id.subTitle)
        val sub = resources.getString(R.string.otpSubtitle,getMobile)
        subtitle.text = sub ?: ""

        sendOtp(getMobile,snackBarViw)

        resendBtn.setOnClickListener {
            sendOtp(getMobile,snackBarViw)
        }

        verifyBtn.setOnClickListener {

            if (verificationId != null){
                val getPin = pinInput.text.toString()
                val credential = PhoneAuthProvider.getCredential(verificationId!!,getPin)
                signInWithPhoneAuthCredential(credential,snackBarViw)
            }
        }

    }


    private fun sendOtp(mobile:String,view: View){
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(mobile) // Phone number to verify
            .setTimeout(30L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    showSnackBar(view,"login successful")
                    auth?.firebaseAuthSettings?.setAppVerificationDisabledForTesting(true)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // This callback is invoked in an invalid request for verification is made,
                    // for instance if the the phone number format is not valid.
                    if (e is FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        showSnackBar(view,"Invalid request")
                    } else if (e is FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        showSnackBar(view,"Sms quota exhausted")
                    } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                        // reCAPTCHA verification attempted with null Activity
                        showSnackBar(view,"Recaptcha issue")
                    }

                }

                override fun onCodeSent(verficationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = verficationId
                    showSnackBar(view,"code sent!")
                }

            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        timer?.cancel()
        resendBtn.isEnabled = false
        //resend code hide upTo 30seconds
        val timeoutMillis = 30000L
         timer = Timer()
         timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    resendBtn.isEnabled = true
                }
            }
        },timeoutMillis)
    }

    fun showSnackBar(view: View,msg:String){
        val snackBar = Snackbar.make(view,msg, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view
        val param = snackBarView.layoutParams as CoordinatorLayout.LayoutParams
        param.gravity = Gravity.TOP
        snackBarView.layoutParams = param
        snackBar.show()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,view: View) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = task.result?.user
                    val intent = Intent(this,MainActivity::class.java)
                    try {
                        startActivity(intent)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        showSnackBar(view,"Invalid Code")
                    }
                    // Update UI
                }
            }
    }



}