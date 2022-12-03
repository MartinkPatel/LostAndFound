package com.martin_kirtan.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.martin_kirtan.lostandfound.databinding.ActivityFeedFoundItemsBinding
import com.martin_kirtan.lostandfound.firestore.MyAdapterFoundItems
import com.martin_kirtan.lostandfound.models.FoundItems

class FeedFoundItems : AppCompatActivity() {
    private lateinit var binding: ActivityFeedFoundItemsBinding
    private lateinit var userArrayList: ArrayList<FoundItems>
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapterFoundItems: MyAdapterFoundItems
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedFoundItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.recyclerview_FoundFeed)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        userArrayList = arrayListOf()

        myAdapterFoundItems = MyAdapterFoundItems(this, userArrayList)

        recyclerView.adapter = myAdapterFoundItems

        eventChangeListener()

        binding.textView4.setOnClickListener {
            finish()
        }


    }

    private fun eventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("Found Items").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error!= null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(FoundItems::class.java))
                    }
                }
                myAdapterFoundItems.notifyDataSetChanged()
            }

        })



    }
}