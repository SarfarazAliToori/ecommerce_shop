package com.acclivousbyte.shopee.models.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequestModel(
    @SerialName("address")
    val address: String,
    @SerialName("email")
    val email: String,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("heard_about_us_from")
    val heard_about_us_from: String? = null,
    @SerialName("is_vendor")
    val is_vendor: String? = null,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("password")
    val password: String,
    @SerialName("password_confirmation")
    val password_confirmation: String,
    @SerialName("phone_number")
    val phone_number: String,
    @SerialName("reference_name")
    val reference_name: String? = null,
    @SerialName("username")
    val username: String? = null
)