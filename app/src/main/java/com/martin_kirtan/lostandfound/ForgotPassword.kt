package com.martin_kirtan.lostandfound

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.martin_kirtan.lostandfound.databinding.ActivityForgotPasswordBinding
import com.martin_kirtan.lostandfound.databinding.ActivityLoginBinding

class ForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private lateinit var actionBar: androidx.appcompat.app.ActionBar

    private lateinit var progressDialog: ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth

    private  var email=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title="Forgot Password "

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Sending Email!..")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.submitBtn.setOnClickListener {
            email = binding.forgotEmailEt.text.toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email!..", Toast.LENGTH_SHORT).show()
            }
            else{
                    progressDialog.show()
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener {task->
                            if(task.isSuccessful){
                                progressDialog.dismiss()
                                Toast.makeText(this, "Password Reset Link sent by email.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            else{
                                progressDialog.dismiss()
                                Toast.makeText(this, "${task.exception!!.message.toString()}", Toast.LENGTH_SHORT).show()

                            }

                        }


            }


        }
    }


}