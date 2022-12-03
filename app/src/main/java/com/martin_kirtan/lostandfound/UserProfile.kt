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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.martin_kirtan.lostandfound.databinding.ActivityPostLostItemBinding
import com.martin_kirtan.lostandfound.databinding.ActivityUserProfileBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass
import com.martin_kirtan.lostandfound.models.User

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
                if (img != null.toString()){
                    Glide.with(this)
                        .load(img).into(profile_img)
                }


            }
        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
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
            startActivity(Intent(this,UpdatePassword::class.java))
        }

        binding.textView4.setOnClickListener {
            finish()
        }

    }
    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}