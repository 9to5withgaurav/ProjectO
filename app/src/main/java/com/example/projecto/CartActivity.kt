package com.example.projecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CartActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private var dogName:String? = null
    private var dogImgSrc:String? = null
    private lateinit var cartImg:ImageView
    private lateinit var cartInfo:TextView
    private var getPickDate:String? = null
    private var user:FirebaseUser? = null
    private var dogOwner:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
         cartImg = findViewById(R.id.dogImg)
         cartInfo = findViewById(R.id.postInfo)
        val getBundle = intent.extras
        val getResultArray = getBundle?.getStringArray("resultArray")
        val getPostId = getResultArray?.get(0)
        getPickDate = getResultArray?.get(1)

        user = Firebase.auth.currentUser

        val fetchData = Thread(object :Runnable{
            override fun run() {
                db.collection("post").document("$getPostId").get()
                    .addOnSuccessListener {document->
                        dogName = document.get("dogName").toString()
                        dogImgSrc = document.get("dogImg").toString()
                        dogOwner = document.get("dogOwner").toString()
                        updateUi(dogName,dogImgSrc)
                    }
                    .addOnFailureListener {e->
                        e.printStackTrace()
                    }
            }
        })
      fetchData.start()

      val paymentRequest = Thread(object : Runnable {
          override fun run() {
              val orderRequest = hashMapOf(
                  "postId" to "$getPostId" ,
                  "userMob" to "${user?.phoneNumber}",
                  "beneficiary" to mutableListOf("$dogName","$dogOwner"),
                  "sponsorDate" to "$getPickDate",
                  "paymentStatus" to "unpaid"
              )
              db.collection("orderRequest").document()
                  .set(orderRequest)
                  .addOnSuccessListener {
                      Log.d("REQUEST","Added Successfully")
                  }
                  .addOnFailureListener {e->
                      e.printStackTrace()
                  }
          }
      })

        findViewById<Button>(R.id.payBtn).setOnClickListener {

            try{
                paymentRequest.start()
                val bundle = bundleOf()
                bundle.putString("pickDate","$getPickDate")
                val intent = Intent(this,ConfirmationActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }catch (e: Exception) {
                e.printStackTrace()
            }

      }

    }

    private fun updateUi(name:String?,src:String?){
        cartImg.load(src){
            placeholder(R.drawable.banner)
        }
        cartInfo.text = resources.getString(R.string.cart_post_info,name,getPickDate)
    }
}