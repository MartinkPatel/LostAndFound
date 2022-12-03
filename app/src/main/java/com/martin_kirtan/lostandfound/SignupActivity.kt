package com.martin_kirtan.lostandfound

import android.app.ActionBar
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.martin_kirtan.lostandfound.databinding.ActivityLoginBinding
import com.martin_kirtan.lostandfound.databinding.ActivitySignupBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass
import com.martin_kirtan.lostandfound.models.User
import java.text.SimpleDateFormat
import java.util.*

class SignupActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySignupBinding

        private lateinit var actionBar: androidx.appcompat.app.ActionBar

        private lateinit var progressDialog: ProgressDialog
    private lateinit var storage: FirebaseStorage


        private lateinit var firebaseAuth: FirebaseAuth
        private var name=""
        private var roll=""
        private var email:String=""
        private var password=""
        private var cnf_password=""
        private var userid=""
        private var phone=""
    private  var foundImage1: Uri? = null
    private var foundImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignupBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        actionBar=supportActionBar!!
//        actionBar.title="Sign Up"
//        actionBar.setDisplayHomeAsUpEnabled(true)
//        actionBar.setDisplayShowHomeEnabled(true)

        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account!..")
        progressDialog.setCanceledOnTouchOutside(false)

        val mFirestore= FirebaseFirestore.getInstance()
        firebaseAuth=FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()


        binding.regUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
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

                    uploadimg()

                    val firebaseUser=firebaseAuth.currentUser
                    val email=firebaseUser!!.email
//                    val user=User(
//                        firebaseUser!!.uid,
//                        name,
//                        roll,
//                        email.toString(),
//                        phone,
//                        foundImageUrl.toString(),
//
//                    )
                    val data= hashMapOf(
                        "id" to FirestoreClass().getCurrentUserId(),
                        "name" to name,
                        "roll" to roll,
                        "email" to email,
                        "phone" to phone,
                        "url" to foundImageUrl
                    )
                    val mFirestore=FirebaseFirestore.getInstance()
                    mFirestore.collection(Constants.USERS).document(firebaseUser!!.uid)
                        .set(data)
//                    FirestoreClass().registerUser(this,user)
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

    private fun uploadimg() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)

        val storage=FirebaseStorage.getInstance().getReference("images/$name/$filename/profile_pic")
        if(foundImage1!=null){
            storage.putFile(foundImage1!!).addOnSuccessListener{
                Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show()

                storage.downloadUrl.addOnSuccessListener {url->
                  //  Toast.makeText(this, "${url.toString()}", Toast.LENGTH_SHORT).show()
                        foundImageUrl=url.toString()
                 //   Toast.makeText(this,"$foundImageUrl",Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener {
                        Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
          onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage1 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, foundImage1)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.img.setBackgroundDrawable(bitmapDrawable)
            binding.img.setImageURI(foundImage1)
        }

    }
}