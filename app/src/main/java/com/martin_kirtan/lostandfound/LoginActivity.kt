package com.martin_kirtan.lostandfound

import android.app.ActionBar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.Nullable
import com.google.firebase.auth.FirebaseAuth
import com.martin_kirtan.lostandfound.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    private lateinit var actionBar: androidx.appcompat.app.ActionBar

    private lateinit var progressDialog:ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private  var email=""
    private var password=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title="Login "

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In!..")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth= FirebaseAuth.getInstance()
        checkUsr()

        binding.signupbtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))

        }
        binding.loginbtn.setOnClickListener{

                validateData()

        }

        binding.forgotpwsbtn.setOnClickListener {
            startActivity(Intent(this,ForgotPassword::class.java))
        }

    }

    private fun validateData() {
        email=binding.emailet.text.toString().trim()
        password=binding.passwordet.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailet.error="Invalid Email Address!"
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordet.error="Please Enter Password"
        }
        else{
            firebaselogin()
        }
        
    }

    private fun firebaselogin() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                val firebaseUser=firebaseAuth.currentUser
                val email=firebaseUser!!.email
                Toast.makeText(this,"Logged in as ${email}",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,HomePage::class.java))
                finish()

            }
            .addOnFailureListener{ e->
                progressDialog.dismiss()

                Toast.makeText(this,"Login Failed due to ${e.message}",Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUsr() {
    val firebaseUser=firebaseAuth.currentUser
        
        if(firebaseUser!=null){
            startActivity(Intent(this, HomePage::class.java))
            finish()
        }
    }
}