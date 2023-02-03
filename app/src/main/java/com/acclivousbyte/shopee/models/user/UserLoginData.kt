package com.acclivousbyte.shopee.models.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginData(
    @SerialName("address")
    val address: String,
    @SerialName("cart")
    val cart: List<String>,
    @SerialName("email")
    val email: String,
    @SerialName("email_verified")
    val email_verified: String,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("id")
    val id: Int,
    @SerialName("is_draft")
    val is_draft: String,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("phone_number")
    val phone_number: String,
    @SerialName("settings")
    val settings: Settings,
    @SerialName("token")
    val token: String,
    @SerialName("username")
    val username: String,
    @SerialName("wishlist")
    val wishlist: List<String>
)

@Serializable
class Settings