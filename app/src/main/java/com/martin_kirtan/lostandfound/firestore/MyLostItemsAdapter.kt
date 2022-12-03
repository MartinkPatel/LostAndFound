package com.martin_kirtan.lostandfound.firestore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_kirtan.lostandfound.R
import com.martin_kirtan.lostandfound.models.LostItems

class MyLostItemsAdapter (
    private val context: Context,
    private val myLostThingsList: ArrayList<LostItems>
) :
    RecyclerView.Adapter<MyLostItemsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Name: TextView = itemView.findViewById(R.id.textView7)
        val Number: TextView = itemView.findViewById(R.id.textView9)
        val Message: TextView = itemView.findViewById(R.id.textView8)
        val Where: TextView = itemView.findViewById(R.id.textView10)
        val Image1: ImageView = itemView.findViewById(R.id.imageView1)
        val Image2: ImageView = itemView.findViewById(R.id.imageView2)
        val Image3: ImageView = itemView.findViewById(R.id.imageView3)
        val Image4: ImageView = itemView.findViewById(R.id.imageView4)
        val Image5: ImageView = itemView.findViewById(R.id.imageView5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.my_lost_feed_row, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val lostThing = myLostThingsList[position]
        holder.Name.text = myLostThingsList[position].name

        holder.Number.text = myLostThingsList[position].phone
        holder.Message.text = myLostThingsList[position].message
        holder.Where.text = myLostThingsList[position].location

        val url1="https://cdn.pixabay.com/photo/2016/09/09/22/40/bike-1658214__340.jpg"

        val url2="https://cdn.pixabay.com/photo/2016/03/31/23/06/bicycle-1297395__340.png"
        val url3="https://media.istockphoto.com/id/1340571998/photo/man-rides-a-bike-outdoors-in-the-park-on-a-sunny-day-at-sunset.jpg?b=1&s=170667a&w=0&k=20&c=2kpssLbHrNJEQVZ8lVWxuw5MEYDk9afMLv1lY1OEL3Q="

        Glide.with(context)
            .load(url1).into(holder.Image1)
        Glide.with(context)
            .load(url2).into(holder.Image2)
        Glide.with(context)
            .load(url3).into(holder.Image3)
        Glide.with(context)
            .load(lostThing.image4Url).into(holder.Image4)
        Glide.with(context)
            .load(lostThing.image5Url).into(holder.Image5)
    }

    override fun getItemCount(): Int {
        return myLostThingsList.size
    }
}

