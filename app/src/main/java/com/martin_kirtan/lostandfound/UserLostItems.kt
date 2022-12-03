package com.martin_kirtan.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.martin_kirtan.lostandfound.databinding.ActivityUserLostItemsBinding
import com.martin_kirtan.lostandfound.firestore.MyLostItemsAdapter
import com.martin_kirtan.lostandfound.models.LostItems

class UserLostItems : AppCompatActivity() {
    private lateinit var binding: ActivityUserLostItemsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var thingsList: ArrayList<LostItems>
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserLostItemsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_user_lost_items)
        recyclerView = findViewById(R.id.recyclerview_my_lost_items)
        recyclerView.layoutManager = LinearLayoutManager(this@UserLostItems)

        thingsList = arrayListOf()

        binding.textView4.setOnClickListener {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
            finish()
        }

        fetchData()

    }

    private fun fetchData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.USERS).document(userID).collection("Lost Items").get().addOnSuccessListener {
            if (!it.isEmpty) {
                for (data in it.documents) {
                    db.collection("user").document()
                    val thing: LostItems? = data.toObject(LostItems::class.java)
                    if (thing != null) {
                        thingsList.add(thing)
                    }
                }
                recyclerView.adapter = MyLostItemsAdapter(this, thingsList)
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }


}