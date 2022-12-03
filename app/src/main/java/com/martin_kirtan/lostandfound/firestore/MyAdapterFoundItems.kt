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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.martin_kirtan.lostandfound.APIService
import com.martin_kirtan.lostandfound.R
import com.martin_kirtan.lostandfound.models.FoundItems
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAdapterFoundItems(private val context: android.content.Context, private val foundItemsList: ArrayList<FoundItems>) : RecyclerView.Adapter<MyAdapterFoundItems.MyViewHolder>() {

    private lateinit var apiService: APIService

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterFoundItems.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.found_feed_row, parent, false)

        return MyViewHolder(itemView)

    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: MyAdapterFoundItems.MyViewHolder, position: Int) {

        val item: FoundItems = foundItemsList[position]
        holder.fullName.text= item.name
        holder.phoneNumber.text = item.phone
        holder.locationFound.text = item.location
        holder.message.text= item.message

        val userID = foundItemsList[position].userID

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService::class.java)

        holder.message.canScrollVertically(1)

        holder.foundBt.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Tokens").child(userID!!).child("token")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val usertoken: String = snapshot.getValue(String::class.java).toString()
                        sendNotification(
                            usertoken,
                            "Lost and Found App",
                            "Someone wants to claim an item you found!"
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            Toast.makeText(context, "clicked $position", Toast.LENGTH_SHORT).show()
        }
        updateToken()
        val url1="data:image/jpeg;base64,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"
        val url2="data:image/jpeg;base64,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"
        Glide.with(context).load(url1).into(holder.image1)
        Glide.with(context).load(url2).into(holder.image2)
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
        apiService.sendNotifcation(sender)!!.enqueue(object : Callback<MyResponse?> {

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
        return foundItemsList.size
    }

    // Holds the views for adding it to image and text
    class MyViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val fullName: TextView = itemView.findViewById(R.id.textView7)
        val phoneNumber: TextView = itemView.findViewById(R.id.textView9)
        val locationFound: TextView = itemView.findViewById(R.id.textView8)
        val message: TextView = itemView.findViewById(R.id.textView10)
        val image1: ImageView = itemView.findViewById(R.id.imageView1)
        val image2: ImageView = itemView.findViewById(R.id.imageView2)
        val image3: ImageView = itemView.findViewById(R.id.imageView3)
        val image4: ImageView = itemView.findViewById(R.id.imageView4)
        val image5: ImageView = itemView.findViewById(R.id.imageView5)
        val foundBt: Button = itemView.findViewById(R.id.button3)

    }
}