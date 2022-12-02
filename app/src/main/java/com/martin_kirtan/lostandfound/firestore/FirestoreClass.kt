package com.martin_kirtan.lostandfound.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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



}