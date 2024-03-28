package com.example.edistynytmobiiliohjelmointi2023.basicauth

import com.google.gson.annotations.SerializedName


data class BasicAuth (

  @SerializedName("user_id"      ) var userId      : String? = null,
  @SerializedName("name"         ) var name        : String? = null,
  @SerializedName("age"          ) var age         : String? = null,
  @SerializedName("email"        ) var email       : String? = null,
  @SerializedName("image"        ) var image       : String? = null,
  @SerializedName("date_created" ) var dateCreated : String? = null

)