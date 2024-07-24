package com.example.projecto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class OrderCart : AppCompatActivity() {
    private val cartList:MutableList<CartObj> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_cart)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val cartRecycler = findViewById<RecyclerView>(R.id.recycle)
        cartRecycler.layoutManager = LinearLayoutManager(this)
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val adapter = OrderCartAdapter(cartList,this)
        cartRecycler.adapter = adapter
         val fetchCartInfo = Thread(object : Runnable {
            override fun run() {
                user?.uid?.let { db.collection("orderRequest").get()
                    .addOnSuccessListener {snapshot ->
                        for ( document in snapshot){
                            if (document["userMob"] == user.phoneNumber){
                               cartList.add(element = CartObj(
                                   postId = document["postId"].toString(),
                                   userMob = document["userMob"].toString(),
                                   paymentStatus = document["paymentStatus"].toString(),
                                   sponsorDate = document["sponsorDate"].toString(),
                                   beneficiary = document["beneficiary"] as MutableList<String>

                               ))
                            }
                           adapter.notifyDataSetChanged()
                        }
                    }
                    .addOnFailureListener {e->
                       e.printStackTrace()
                    }
                }
            }

        })

        fetchCartInfo.start()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}