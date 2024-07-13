package com.example.projecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import coil.load
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import de.hdodenhof.circleimageview.CircleImageView


class PostInfoActivity : AppCompatActivity() {
    private lateinit var postObj: PostObj
    private var about: TextView? = null
    private var address: TextView? = null
    private var name: TextView? = null
    private var fetchOwner:String? = null
    private var dogImg:CircleImageView? = null
    private var sponsorUpd:TextView? = null
    private var sponsorBtn: Button? = null
    private var getPostId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption_info)
        about = findViewById(R.id.aboutInfo)
        address = findViewById(R.id.shelterAddress)
        name = findViewById(R.id.shelterName)
        dogImg = findViewById(R.id.dogImg)
        sponsorUpd = findViewById(R.id.sponsorUpd)
        sponsorBtn = findViewById(R.id.sponsorBtn)
        val bundle = intent.extras
        getPostId = bundle?.getString("post_id")
        val db = Firebase.firestore

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val fetchDBThread = Thread(object : Runnable {
            override fun run() {
               db.collection("post")
                   .get()
                   .addOnSuccessListener {documents->
                     for (doc in documents){
                         if (doc["postId"] == getPostId ){
                             val fetchPostObj = doc.toObject<PostObj>()
                             updateUi(fetchPostObj)
                         }else{
                             Log.d("NOOOOOO","Oh no ")
                         }
                     }
                   }
                   .addOnFailureListener {e->
                      e.printStackTrace()
                   }

            }

        })

        fetchDBThread.start()

    }

    private fun updateUi(postObj: PostObj){
       about?.text = postObj.dogDes
       name?.text = postObj.dogOwner
       address?.text = postObj.ownerAddress
        dogImg?.load(postObj.dogImg)
        val string = String.format(resources.getString(R.string.sponsor_info),postObj.dogName)
        sponsorUpd?.text = string
        sponsorBtn?.setOnClickListener {
            try {
                val bundle = bundleOf()
                bundle.putString("postId","$getPostId")
                val intent = Intent(this,SponsorFormActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

 }


