package com.martin_kirtan.lostandfound

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.martin_kirtan.lostandfound.databinding.ActivityFeedLostItemsBinding
import com.martin_kirtan.lostandfound.firestore.AdapterLostItems
import com.martin_kirtan.lostandfound.models.LostItems

class FeedLostItems : AppCompatActivity() {
    private lateinit var binding: ActivityFeedLostItemsBinding
    private lateinit var userArrayList: ArrayList<LostItems>
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapterLostItems: AdapterLostItems
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedLostItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    val mFirestore=FirebaseFirestore.getInstance()

    binding.recyclerviewLostFeed.layoutManager=LinearLayoutManager(this)
        binding.recyclerviewLostFeed.setHasFixedSize(true)

        userArrayList= arrayListOf()

        myAdapterLostItems=AdapterLostItems(this,userArrayList)

        recyclerView.adapter=myAdapterLostItems

        eventChangeListener()

        binding.textView4.setOnClickListener {
            finish()
        }



    }
    private fun eventChangeListener(){

        db = FirebaseFirestore.getInstance()
        db.collection("Lost Items").addSnapshotListener(object: EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error!= null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc: DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        userArrayList.add(dc.document.toObject(LostItems::class.java))
                    }
                }
                myAdapterLostItems.notifyDataSetChanged()
            }

        })

    }
}