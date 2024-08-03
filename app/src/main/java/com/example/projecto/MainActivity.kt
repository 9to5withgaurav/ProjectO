package com.example.projecto

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner

const val KEY = "auth"
const val  MAIN = "MainActivity"


class MainActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private val dataList: MutableList<PostObj> = mutableListOf()
    private lateinit var adapter:PostAdapter
    private lateinit var recyclerView: RecyclerView
    private var data:PostObj? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val sharedPref = getSharedPreferences("userID", Context.MODE_PRIVATE)
        val fetch = sharedPref.getString(KEY,null)
        Log.i(MAIN,"$fetch")
        recyclerView = findViewById(R.id.recyecler)
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = PostAdapter(dataSet = dataList, object : PostAdapter.OnItemCLickListener {
            override fun onItemClick(postObj: PostObj) {
                try{
                    val bundle = Bundle()
                    bundle.putString("post_id",postObj.postId)
                    val intent = Intent(this@MainActivity,PostInfoActivity::class.java)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }catch(e: Exception){
                    e.printStackTrace()
                }
            }

        })

        recyclerView.adapter = adapter

        fetchRecycleViewThread()

       fab.setOnClickListener {
          try {
              val intent = Intent(this,OrderCart::class.java)
              startActivity(intent)
          }catch (e:Exception){
              e.printStackTrace()
          }
       }

    }

    private fun fetchRecycleViewThread(){
    val myThread = Thread {
        db.collection("post")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    data = document.toObject(PostObj::class.java)
                    dataList.add(data!!)

                }
                adapter.notifyDataSetChanged()

            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

        myThread.start()

    }


}


