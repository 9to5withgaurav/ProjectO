package com.example.projecto

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

const val TAG = "LoginScreen"

class LoginScreen : Fragment() {
    private lateinit var view: View
    private var auth: FirebaseAuth? = null
    private lateinit var mobileNo: String
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted:$credential")
            auth!!.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            val bundle = bundleOf("verificationId" to verificationId)
            findNavController().navigate(R.id.action_loginScreen_to_authVerificationScreen, bundle)
            // Log.d(TAG, "onCodeSent:$verificationId")

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_screen, container, false)
        auth = FirebaseAuth.getInstance()
        return view.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val continueBtn = view.findViewById<Button>(R.id.continueBtn)
        val getMobileInputText = view.findViewById<TextInputEditText>(R.id.phoneText)

        continueBtn.setOnClickListener {
            mobileNo = getMobileInputText.text.toString()
            getUserMobileNo(mobileNo)
            Log.d(TAG, mobileNo)
        }
    }

    private fun getUserMobileNo(mobileDigits: String) {
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(mobileDigits) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    findNavController().navigate(R.id.homeScreen)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Log.e(TAG, "Verification Code invalid")
                    }
                    // Update UI
                    Snackbar.make(view, "Welcome Admin", Snackbar.LENGTH_SHORT).show()
                }
            }
    }
}