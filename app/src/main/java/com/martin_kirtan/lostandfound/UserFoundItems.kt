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
import com.martin_kirtan.lostandfound.databinding.ActivityUserFoundItemsBinding
import com.martin_kirtan.lostandfound.firestore.MyFoundItemsAdapter
import com.martin_kirtan.lostandfound.models.FoundItems

class UserFoundItems : AppCompatActivity() {
    private lateinit var binding: ActivityUserFoundItemsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var thingsList: ArrayList<FoundItems>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserFoundItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.recyclerview_my_found_items)
        recyclerView.layoutManager = LinearLayoutManager(this@UserFoundItems)

        thingsList = arrayListOf()

        binding.textView4.setOnClickListener {
            startActivity(Intent(this,HomePage::class.java))
            finish()
        }

        fetchData()
    }

    private fun fetchData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseFirestore.getInstance()
        db.collection(Constants.USERS).document(userID).collection("Found Items").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val things: FoundItems? = data.toObject(FoundItems::class.java)
                        if (things != null) {
                            thingsList.add(things)
                        }
                        recyclerView.adapter = MyFoundItemsAdapter(this, thingsList)
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}