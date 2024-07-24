package com.example.projecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SponsorFormActivity : AppCompatActivity() {
    private var user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var getPostId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sponsor_form)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bundle = intent.extras
        getPostId = bundle?.getString("postId")
        val db = Firebase.firestore
        val dateBtn = findViewById<Button>(R.id.pickDate)
        val donateBtn = findViewById<Button>(R.id.donateBtn)
        val name = findViewById<EditText>(R.id.name)
        val address = findViewById<EditText>(R.id.address)
        val phone = findViewById<EditText>(R.id.phone)


        val dateField =findViewById<TextView>(R.id.chooseDate)
        dateBtn.setOnClickListener {
            val newFragment = DatePickerFragment(dateField,donateBtn)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        donateBtn.setOnClickListener {

            if (name.text.isNullOrEmpty() || address.text.isNullOrEmpty() || phone.text.isNullOrEmpty() || dateField.text.isNullOrEmpty()){
                alertBox("Field can't be empty")
            }else{
                val randomCode = generateRandomCode(10)
                val sponsorRequest = hashMapOf(
                    "sponsorId" to randomCode,
                    "name" to name.text.toString(),
                    "address" to address.text.toString(),
                    "phone" to phone.text.toString(),
                    "sponsorDate" to dateField.text.toString(),
                    "postId" to getPostId,

                    )

                db.collection("sponsorRequest")
                    .document()
                    .set(sponsorRequest)
                    .addOnSuccessListener {document->
                        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                        builder
                            .setMessage("Thank you for your meal sponsorship on ${sponsorRequest["sponsorDate"]}." +
                                    "Our Executive will call you for complete your payment  " +
                                    "")
                            .setTitle("Confirmation")
                            .setPositiveButton("Ok") { dialog, which ->
                                val bundle = bundleOf()
                                bundle.putString("post_id",getPostId)
                                val intent = Intent(this,PostInfoActivity::class.java)
                                intent.putExtras(bundle)
                                startActivity(intent)
                                dialog.dismiss()
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                    .addOnFailureListener {e->
                        e.printStackTrace()
                    }
            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
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

    fun generateRandomCode(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }


}