package com.martin_kirtan.lostandfound.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

class MyFirebaseIdService: FirebaseMessagingService() {
    override fun onNewToken(s:String){
        super.onNewToken(s)
        var firebaseUser = FirebaseAuth.getInstance().currentUser
        var refreshToken:String = FirebaseMessaging.getInstance().token.toString()
        if(firebaseUser!=null){
            updateToken(refreshToken)
        }
    }
    private fun updateToken(refreshToken:String){
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        var token: Token = Token(refreshToken)
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser()!!.getUid()).setValue(token)
    }
}