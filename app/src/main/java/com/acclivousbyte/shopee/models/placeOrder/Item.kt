package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("id")
    val id: String?,
    @SerialName("quantity")
    val quantity: String?,
    @SerialName("type")
    val type: String?
)
//
//@Serializable
//data class Item(
//    @SerialName("id")
//    val id: Int?,
//    @SerialName("quantity")
//    val quantity: String?,
//    @SerialName("type")
//    val type: String?
//)