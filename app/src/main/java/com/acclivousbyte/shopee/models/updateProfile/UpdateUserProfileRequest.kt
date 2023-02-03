package com.acclivousbyte.shopee.models.updateProfile


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    @SerialName("address")
    val address: String,
    @SerialName("email")
    val email: String,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("phone_number")
    val phone_number: String,
    @SerialName("user_id")
    val user_id: Int
)