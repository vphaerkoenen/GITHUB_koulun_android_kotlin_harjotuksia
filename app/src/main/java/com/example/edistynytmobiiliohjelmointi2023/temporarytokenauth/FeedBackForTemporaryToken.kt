package com.example.edistynytmobiiliohjelmointi2023.temporarytokenauth

import com.google.gson.annotations.SerializedName


data class FeedBackForTemporaryToken (

    @SerializedName("id"       ) var id       : Int?    = null,
    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("location" ) var location : String? = null,
    @SerializedName("value"    ) var value    : String? = null

)