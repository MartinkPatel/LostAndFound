package com.martin_kirtan.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.martin_kirtan.lostandfound.databinding.ActivityHomePageBinding

class HomePage : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageBinding

    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar=supportActionBar!!
        actionBar.title="Home Page"

        firebaseAuth= FirebaseAuth.getInstance()
        checkUsr()

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUsr()
        }
    }

    private fun checkUsr() {
        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser!=null){
            val email=firebaseUser.email
            binding.nameTv.text=email


        }
        else{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }
}