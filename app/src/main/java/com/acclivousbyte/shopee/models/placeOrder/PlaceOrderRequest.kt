package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderRequest(
    @SerialName("user_id")
    val user_id: Int?,
    @SerialName("items")
    val items: List<Item>? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("phone_number")
    val phone_number: String? = null,
    val cardToken: String?
)

//@Serializable
//data class PlaceOrderRequest(
//    @SerialName("address")
//    val address: String? = null,
//    @SerialName("items")
//    val items: List<Item>? = null,
//    @SerialName("phone_number")
//    val phoneNumber: String? = null,
//    @SerialName("user_id")
//    val user_id: Int?,
//    val cardToken: String?
//)