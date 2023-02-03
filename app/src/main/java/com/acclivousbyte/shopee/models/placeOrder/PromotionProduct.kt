package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionProduct(
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String? = null
)