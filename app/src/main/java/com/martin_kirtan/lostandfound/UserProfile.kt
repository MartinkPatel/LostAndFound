package com.martin_kirtan.lostandfound

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.martin_kirtan.lostandfound.databinding.ActivityPostLostItemBinding
import com.martin_kirtan.lostandfound.databinding.ActivityUserProfileBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass
import com.martin_kirtan.lostandfound.models.User
import com.squareup.picasso.Picasso

class UserProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var profile_name: TextView
    private lateinit var profile_rollno: TextView
    private lateinit var profile_number: TextView
    private lateinit var profile_email: TextView
    private lateinit var profile_img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        profile_name = findViewById(R.id.user_profile_name)
        profile_rollno = findViewById(R.id.user_rollno)
        profile_number = findViewById(R.id.user_number)
        profile_email = findViewById(R.id.user_email)
        profile_img = findViewById(R.id.profile_img_profile)

        val mFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        val userId=FirestoreClass().getCurrentUserId()
        var name=""
        var roll=""
        var email=""
        var img:String=""
        var phone:String=""
        mFirestore.collection(Constants.USERS)
            .document(userId)
            .get()
            .addOnSuccessListener {document->
                var user=document.toObject(User::class.java)!!
                 name="${user.name}"
                 roll ="${user.roll}"
                 email="${user.email}"
                 img="${user.url}"
                phone="${user.phone}"
                binding.userProfileName.text=name.toString().toEditable()
                binding.userNumber.text=phone.toString().toEditable()
                binding.userEmail.text=email.toString().toEditable()
                binding.userRollno.text=roll.toString().toEditable()

                val image_url="https://c8.alamy.com/comp/2JK4TGD/homer-simpson-the-simpsons-movie-2007-2JK4TGD.jpg"
                Glide.with(this).load(image_url).into(profile_img)
            }
        binding.btnLogout.setOnClickListener {
            var userID=userId
            firebaseAuth.signOut()
            clearToken(userID)
            
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

        binding.btnMyFoundItems.setOnClickListener {
            startActivity(Intent(this,UserFoundItems::class.java))
        }
        binding.btnMyLostItems.setOnClickListener {
            startActivity(Intent(this,UserLostItems::class.java))
        }
        binding.btnUpdatePass.setOnClickListener{
            startActivity(Intent(this,ForgotPassword::class.java))
        }

        binding.textView4.setOnClickListener {
            finish()
        }

    }

    private fun clearToken(userID: String) {

        FirebaseDatabase.getInstance().getReference("tokens").child(userID)
            .removeValue()
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}