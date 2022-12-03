package com.martin_kirtan.lostandfound.firestore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.phone.SmsRetriever.getClient
import com.google.android.gms.common.api.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.martin_kirtan.lostandfound.APIService
import com.martin_kirtan.lostandfound.R
import com.martin_kirtan.lostandfound.models.LostItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdapterLostItems (private val context: android.content.Context, private val lostItemsList: ArrayList<LostItems>) : RecyclerView.Adapter<AdapterLostItems.MyViewHolder>() {

    private lateinit var apiService: APIService

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterLostItems.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lost_feed_row, parent, false)

        return MyViewHolder(itemView)

    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: AdapterLostItems.MyViewHolder, position: Int) {

        val item: LostItems = lostItemsList[position]
        holder.fullName.text= item.name
        holder.phoneNumber.text = item.phone
        holder.locationLost.text = item.location
        holder.message.text= item.message

        val userID = lostItemsList[position].userID

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService::class.java)

        holder.lostBt.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Tokens").child(userID!!).child("token")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usertoken: String = snapshot.getValue(String::class.java).toString()
                        sendNotification(
                            usertoken,
                            "Lost and Found App",
                            "Someone found your lost item!"
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
        updateToken()


        Glide.with(context).load(item.image1Url).into(holder.image1)
        Glide.with(context).load(item.image2Url).into(holder.image2)
        Glide.with(context).load(item.image3Url).into(holder.image3)
        Glide.with(context).load(item.image4Url).into(holder.image4)
        Glide.with(context).load(item.image5Url).into(holder.image5)

    }

    private fun updateToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val token: String? = it.result
                    val userID = FirebaseAuth.getInstance().currentUser!!.uid
                    FirebaseDatabase.getInstance().getReference("Tokens").child(userID).child("token").setValue(token)
                }
            }
    }

    private fun sendNotification(usertoken:String,title: String,message: String){
        val data=Data(title,message)
        val sender= NotificationSender(data,usertoken)
        apiService.sendNotifcation(sender)!!.enqueue(object : retrofit2.Callback<MyResponse?> {

            override fun onResponse(call: Call<MyResponse?>, response: Response<MyResponse?>) {
                if (response.code() == 200) {
                    if (response.body()!!.success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<MyResponse?>, t: Throwable) {
            }
        })
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return lostItemsList.size
    }

    // Holds the views for adding it to image and text
    class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val fullName: TextView = itemView.findViewById(R.id.textView7)
        val phoneNumber: TextView = itemView.findViewById(R.id.textView9)
        val locationLost: TextView = itemView.findViewById(R.id.textView8)
        val message: TextView = itemView.findViewById(R.id.textView10)
        val image1: ImageView = itemView.findViewById(R.id.imageView1)
        val image2: ImageView = itemView.findViewById(R.id.imageView2)
        val image3: ImageView = itemView.findViewById(R.id.imageView3)
        val image4: ImageView = itemView.findViewById(R.id.imageView4)
        val image5: ImageView = itemView.findViewById(R.id.imageView5)
        val lostBt: Button = itemView.findViewById(R.id.button3)


    }
}

