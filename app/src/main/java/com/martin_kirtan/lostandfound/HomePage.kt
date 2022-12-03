package com.martin_kirtan.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.martin_kirtan.lostandfound.databinding.ActivityHomePageBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding
    private lateinit var database: DatabaseReference
    private lateinit var userID: String
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

            //   actionBar=supportActionBar!!
        //actionBar.title="Home Page"

        firebaseAuth= FirebaseAuth.getInstance()
        FirestoreClass().getUserDetails(this)
        val sharedPreferences=getSharedPreferences(Constants.PREFERENCES, MODE_PRIVATE)
        val username=sharedPreferences.getString(Constants.LOG_IN_USERNAME,"")!!
        binding.nameTv.text=username
        checkUsr()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUsr()
        }
        binding.postfoundbtn.setOnClickListener {
            startActivity(Intent(this,PostFoundItem::class.java))

        }
        binding.postlostbtn.setOnClickListener {
            startActivity(Intent(this,PostLostItem::class.java))

        }
        binding.userProfile.setOnClickListener {
            startActivity(Intent(this,UserProfile::class.java))

        }
        binding.feedLostItems.setOnClickListener {
            startActivity(Intent(this,FeedLostItems::class.java))
        }
        userID =FirestoreClass().getCurrentUserId()
        database = FirebaseDatabase.getInstance().getReference(Constants.USERS)
        database.child(userID).get().addOnSuccessListener {
            val fullName = it.child("name").value
            val rollNumber = it.child("roll").value
            val phoneNumber = it.child("phone").value
            //            email.replace(",", ".")
        }

    }

    private fun checkUsr() {
        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser!=null){
            val email=firebaseUser.email
           // binding.nameTv.text=username


        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}