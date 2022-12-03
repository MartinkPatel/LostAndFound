package com.martin_kirtan.lostandfound

import com.martin_kirtan.lostandfound.firestore.MyResponse
import com.martin_kirtan.lostandfound.firestore.NotificationSender
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=	AAAA_eq1leU:APA91bFiKAvwyGDkzUrXIRIKg_3ONH_ChHIeYNtMak-4mhRf7khyeYKLqMPLdq7pIHxpP2XdBfjNctFyiVE8FVUcFXjxEFRChsyoFhaWGWVKCBddK5R2Gbl_-8NUrZ6KH3iUW0dpoK1o"
    )
    @POST("fcm/send")
    fun sendNotifcation(@Body body: NotificationSender?): Call<MyResponse?>?
}
