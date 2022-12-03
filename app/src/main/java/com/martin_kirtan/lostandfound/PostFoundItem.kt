package com.martin_kirtan.lostandfound

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.martin_kirtan.lostandfound.databinding.ActivityPostLostItemBinding
import com.martin_kirtan.lostandfound.firestore.FirestoreClass
import com.martin_kirtan.lostandfound.models.User
import java.text.SimpleDateFormat
import java.util.*

class PostFoundItem : AppCompatActivity() {
    private lateinit var binding: ActivityPostLostItemBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth
    private  var foundImage1: Uri? = null
    private  var foundImage2: Uri? = null
    private  var foundImage3: Uri? = null
    private  var foundImage4: Uri? = null
    private  var foundImage5: Uri? = null

    private lateinit var userID: String
    private lateinit var fullName: String

    private lateinit var etName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etMessage: EditText
    private lateinit var etWhereFound: EditText

    private var foundImage1Url: String? = null
    private var foundImage2Url: String? = null
    private var foundImage3Url: String? = null
    private var foundImage4Url: String? = null
    private var foundImage5Url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPostLostItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth= FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        userID = FirestoreClass().getCurrentUserId()

        etName = findViewById(R.id.nameET)
        etPhoneNumber = findViewById(R.id.whatsappET)
        etMessage = findViewById(R.id.messageET)
        etWhereFound = findViewById(R.id.locationET)
        Toast.makeText(this,"$userID",Toast.LENGTH_SHORT).show()
        val mFirestore= FirebaseFirestore.getInstance()

        mFirestore.collection(Constants.USERS)
            .document(userID)
            .get()
            .addOnSuccessListener {document->
                val user=document.toObject(User::class.java)!!

                fullName="${user.name}"
                val phnumber="${user.phone}"
                etName.text=fullName.toString().toEditable()

                etPhoneNumber.text=phnumber.toString().toEditable()




            }
            .addOnFailureListener {
                Toast.makeText(this,"no",Toast.LENGTH_SHORT).show()
            }

        binding.textView2.setOnClickListener{
            finish()
        }
        binding.imageView1.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)

        }

        binding.imageView2.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 2)

        }

        binding.imageView3.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 3)

        }

        binding.imageView4.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 4)

        }

        binding.imageView5.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 5)

        }

        binding.btnUpload.setOnClickListener {
            if (foundImage1 != null || foundImage2 != null || foundImage3 != null || foundImage4 != null || foundImage5 != null) {
                uploadImage()
            }
        }

        binding.btnSubmit.setOnClickListener{
            val editname=etName.text.toString().trim()
            val editphone=etPhoneNumber.text.toString().trim()
            val location=etWhereFound.text.toString().trim()
            val message=etMessage.text.toString().trim()

            val formatter= SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now=Date()
            val filename=formatter.format(now)

            val data= hashMapOf(
                    "name" to editname,
                    "phone" to editphone,
                    "location" to location,
                    "message" to message,
                    "image1url" to foundImage1Url,
                "image2url" to foundImage2Url,
                "image3url" to foundImage3Url,
                "image4url" to foundImage4Url,
                "image5url" to foundImage5Url,
                "userId" to userID

             )
            mFirestore.collection(Constants.USERS).document(userID).collection("Found Items").document(filename)
                .set(data)
            mFirestore.collection("Found Items").document(filename).set(data)
                .addOnSuccessListener {
                    Toast.makeText(this,"Submitted Successfully",Toast.LENGTH_SHORT).show()

                }

            finish()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage1 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, foundImage1)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imageView1.setBackgroundDrawable(bitmapDrawable)
            binding.imageView1.setImageURI(foundImage1)
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage2 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, foundImage2)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imageView2.setBackgroundDrawable(bitmapDrawable)
            binding.imageView2.setImageURI(foundImage2)
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage3 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, foundImage3)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imageView3.setBackgroundDrawable(bitmapDrawable)
            binding.imageView3.setImageURI(foundImage3)
        }
        if (requestCode == 4 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage4 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, foundImage4)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imageView4.setBackgroundDrawable(bitmapDrawable)
            binding.imageView4.setImageURI(foundImage4)
        }
        if (requestCode == 5 && resultCode == Activity.RESULT_OK && data != null) {
            foundImage5 = data?.data!!
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,foundImage5)
            val bitmapDrawable = BitmapDrawable(bitmap)
            binding.imageView5.setBackgroundDrawable(bitmapDrawable)
            binding.imageView5.setImageURI(foundImage5)
        }

    }
    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Image...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)


        val storage1 = FirebaseStorage.getInstance().getReference("images/$fullName/$filename/1")
        val storage2 = FirebaseStorage.getInstance().getReference("images/$fullName/$filename/2")
        val storage3 = FirebaseStorage.getInstance().getReference("images/$fullName/$filename/3")
        val storage4 = FirebaseStorage.getInstance().getReference("images/$fullName/$filename/4")
        val storage5 = FirebaseStorage.getInstance().getReference("images/$fullName/$filename/5")

        if(foundImage1 != null){
            storage1.putFile(foundImage1!!).addOnSuccessListener {
                Toast.makeText(this, "1st Image uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                storage1.downloadUrl.addOnSuccessListener {
                    foundImage1Url = it.toString()
                }
            }.addOnFailureListener{
                Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }

        if(foundImage2 != null) {
            storage2.putFile(foundImage2!!).addOnSuccessListener {
                Toast.makeText(this, "2nd Image uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                storage2.downloadUrl.addOnSuccessListener {
                    foundImage2Url = it.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }

        if(foundImage3 != null) {
            storage3.putFile(foundImage3!!).addOnSuccessListener {
                Toast.makeText(this, "2nd Image uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                storage3.downloadUrl.addOnSuccessListener {
                    foundImage3Url = it.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }

        if(foundImage4 != null) {
            storage4.putFile(foundImage4!!).addOnSuccessListener {
                storage4.downloadUrl.addOnSuccessListener {
                    Toast.makeText(this, "2nd Image uploaded", Toast.LENGTH_SHORT)
                        .show()
                    if (progressDialog.isShowing) progressDialog.dismiss()
                    foundImage4Url = it.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }

        if(foundImage5 != null) {
            storage5.putFile(foundImage5!!).addOnSuccessListener {
                Toast.makeText(this, "2nd Image uploaded", Toast.LENGTH_SHORT)
                    .show()
                if (progressDialog.isShowing) progressDialog.dismiss()
                storage5.downloadUrl.addOnSuccessListener {
                    foundImage5Url = it.toString()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                if (progressDialog.isShowing) progressDialog.dismiss()
            }
        }
    }


    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}