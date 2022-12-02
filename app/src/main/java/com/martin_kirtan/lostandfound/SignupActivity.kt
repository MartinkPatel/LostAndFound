package com.martin_kirtan.lostandfound

import android.app.ActionBar
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.martin_kirtan.lostandfound.databinding.ActivityLoginBinding
import com.martin_kirtan.lostandfound.databinding.ActivitySignupBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass
import com.martin_kirtan.lostandfound.models.User

class SignupActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySignupBinding

        private lateinit var actionBar: androidx.appcompat.app.ActionBar

        private lateinit var progressDialog: ProgressDialog

        private lateinit var firebaseAuth: FirebaseAuth
        private var name=""
        private var roll=""
        private var email:String=""
        private var password=""
        private var cnf_password=""
        private var phone=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)

        setContentView(binding.root)
        actionBar=supportActionBar!!
        actionBar.title="Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account!..")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth=FirebaseAuth.getInstance()

        binding.regSignupbtn.setOnClickListener {
            validatedata()
        }
    }

    private fun validatedata() {

        name=binding.regName.text.toString().trim()
        roll=binding.regRoll.text.toString().trim()
        email=binding.regEmail.text.toString().trim()
        password=binding.regPws.text.toString().trim()
        cnf_password=binding.regCnfPws.text.toString().trim()
        phone=binding.regPh.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.regEmail.error="Invalid Email Address!"
        }
        else if(!email.endsWith("@iitp.ac.in")){
            binding.regEmail.error="Enter email with @iitp.ac.in."
        }
        else if(TextUtils.isEmpty(password) ){
            binding.regPws.error="Please Enter Password."
        }
        else if(TextUtils.isEmpty(cnf_password) ){
            binding.regCnfPws.error="Please Enter Password."
        }
        else if(TextUtils.isEmpty(name) ){
            binding.regName.error="Please Enter Name."
        }
        else if(TextUtils.isEmpty(roll) ){
            binding.regRoll.error="Please Enter Roll number."
        }
        else if(TextUtils.isEmpty(phone) ){
            binding.regPh.error="Please Enter WA Phone number."
        }
        else if(password!=cnf_password){
            binding.regCnfPws.error="Password not matching."
        }
        else{

            firebaseSignUp()

        }

    }

    private fun firebaseSignUp() {

            progressDialog.show()
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {



                    val firebaseUser=firebaseAuth.currentUser
                    val email=firebaseUser!!.email
                    val user=User(
                        firebaseUser.uid,
                        name,
                        roll,
                        firebaseUser.email!!,
                        phone,

                    )
                    FirestoreClass().registerUser(this,user)
                    progressDialog.dismiss()
                    Toast.makeText(this,"Account Created with email: $email",Toast.LENGTH_SHORT).show()
                    firebaseUser.sendEmailVerification()
                        .addOnSuccessListener{
                            Toast.makeText(this, "Verification email has been sent.!", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener{e->
                            Toast.makeText(this, "Verification email not sent due to ${e.message}", Toast.LENGTH_SHORT).show()

                        }




                    startActivity(Intent(this,verfication::class.java))
                    finish()
                }
                .addOnFailureListener {e->
                        progressDialog.dismiss()
                        Toast.makeText(this,"Sign Up failed due to ${e.message} ",Toast.LENGTH_SHORT).show()

                }
    }

    override fun onSupportNavigateUp(): Boolean {
          onBackPressed()
        return super.onSupportNavigateUp()
    }
}