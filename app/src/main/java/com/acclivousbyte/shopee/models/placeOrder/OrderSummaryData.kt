package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderSummaryData(
    @SerialName("address")
    val address: String? = null,
    @SerialName("created_at")
    val created_at: CreatedAt? = null,
    @SerialName("id")
    val id: Int?,
    @SerialName("items")
    val items: List<ItemsDetails>,
    @SerialName("order_id_string")
    val order_id_string: String? = null,
    @SerialName("phone_number")
    val phone_number: String? = null,
    @SerialName("status")
    val status: String? = null,
    @SerialName("total_price")
    val total_price: Int?,
    @SerialName("type")
    val type: String? = null
): java.io.Serializable