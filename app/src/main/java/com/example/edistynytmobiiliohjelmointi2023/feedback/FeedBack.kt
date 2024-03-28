package com.example.edistynytmobiiliohjelmointi2023.feedback

import com.google.gson.annotations.SerializedName


data class FeedBack (

  @SerializedName("id"       ) var id       : Int?    = null,
  @SerializedName("name"     ) var name     : String? = null,
  @SerializedName("location" ) var location : String? = null,
  @SerializedName("value"    ) var value    : String? = null

)