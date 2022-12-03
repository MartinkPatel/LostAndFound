package com.martin_kirtan.lostandfound.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.martin_kirtan.lostandfound.Constants
import com.martin_kirtan.lostandfound.SignupActivity
import com.martin_kirtan.lostandfound.models.User

class FirestoreClass {

private val mFirestore=FirebaseFirestore.getInstance()

    fun registerUser(activity: SignupActivity, userInfo: User)
    {
        mFirestore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {


               }
            .addOnFailureListener{e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading",
                    e
                )
            }
    }
    fun getCurrentUserId(): String{
        val currentuser=FirebaseAuth.getInstance().currentUser
        var currentID=""
        if(currentID!=null) {
            currentID = currentuser!!.uid
        }
    return currentID
}

    fun getUserDetails(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user=document.toObject(User::class.java)!!

                val sharedPreferences=
                    activity.getSharedPreferences(
                        Constants.PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor=sharedPreferences.edit()
                editor.putString(Constants.LOG_IN_USERNAME,"${user.name}")
                editor.apply()
            }



    }

}