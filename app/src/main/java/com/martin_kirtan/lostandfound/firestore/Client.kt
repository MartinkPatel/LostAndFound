package com.martin_kirtan.lostandfound.firestore

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Client {
    companion object {
        private lateinit var retrofit: Retrofit
        fun getClient(url: String): Retrofit {
            retrofit =
                Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit
        }
    }
}