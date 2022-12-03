package com.martin_kirtan.lostandfound.firestore


class NotificationSender(val data: Data?, val to:String){
    constructor():this(null,""){}
}