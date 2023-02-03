package com.acclivousbyte.shopee.models.userOrders


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromotionProduct(
    @SerialName("id")
    val id: Int?,
    @SerialName("title")
    val title: String?
)