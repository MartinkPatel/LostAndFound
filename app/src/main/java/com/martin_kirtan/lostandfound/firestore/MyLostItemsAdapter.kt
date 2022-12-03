package com.martin_kirtan.lostandfound.firestore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

        Glide.with(context)
            .load(lostThing.image1Url).into(holder.Image1)
        Glide.with(context)
            .load(lostThing.image2Url).into(holder.Image2)
        Glide.with(context)
            .load(lostThing.image3Url).into(holder.Image3)
        Glide.with(context)
            .load(lostThing.image4Url).into(holder.Image4)
        Glide.with(context)
            .load(lostThing.image5Url).into(holder.Image5)
    }

    override fun getItemCount(): Int {
        return myLostThingsList.size
    }
}

