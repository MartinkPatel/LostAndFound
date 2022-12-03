package com.martin_kirtan.lostandfound.models

data class LostItems(
    val name: String? = null,
    val phone: String?,
    val location: String? = null,
    val message: String? = null,
    val image1Url: String? = null,
    val image2Url: String? = null,
    val image3Url: String? = null,
    val image4Url: String? = null,
    val image5Url: String? = null, val userID: String? = null){
    constructor(): this("",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "", "")
}

