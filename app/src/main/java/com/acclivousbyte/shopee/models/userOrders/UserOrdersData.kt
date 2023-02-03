package com.acclivousbyte.shopee.models.userOrders


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserOrdersData(
    @SerialName("address")
    val address: String?,
    @SerialName("created_at")
    val created_at: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("items")
    val items: List<UserOrdersItems>?,
    @SerialName("order_id_string")
    val order_id_string: String?,
    @SerialName("phone_number")
    val phone_number: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("total_price")
    val total_price: Int?,
    @SerialName("type")
    val type: String?
)