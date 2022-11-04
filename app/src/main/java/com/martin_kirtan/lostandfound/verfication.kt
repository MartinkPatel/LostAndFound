package com.martin_kirtan.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.martin_kirtan.lostandfound.databinding.ActivitySignupBinding
import com.martin_kirtan.lostandfound.databinding.ActivityVerficationBinding

class verfication : AppCompatActivity() {
    private  lateinit var bindding: ActivityVerficationBinding
    private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindding=ActivityVerficationBinding.inflate(layoutInflater)
        setContentView(bindding.root)

        firebaseAuth=FirebaseAuth.getInstance()

        val firebaseUser=firebaseAuth.currentUser
        if(firebaseUser!!.isEmailVerified){

            startActivity(Intent(this,HomePage::class.java))
            finish()
        }
        else{

            firebaseUser.sendEmailVerification()
                .addOnSuccessListener{
                    Toast.makeText(this, "Verification email has been sent.!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener{e->
                    Toast.makeText(this, "Verification email not sent due to ${e.message}", Toast.LENGTH_SHORT).show()

                }



        }

        bindding.verifyLgnbtn.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

}