package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemsDetails(
    @SerialName("description")
    val description: String? = null,
    @SerialName("id")
    val id: Int?,
    @SerialName("image_web")
    val image_web: String? = null,
    @SerialName("price")
    val price: Int? = null,
    @SerialName("promotion_products")
    val promotion_products: List<PromotionProduct>? = null,
    @SerialName("quantity")
    val quantity: Int?,
    @SerialName("title")
    val title: String? = null,
    @SerialName("type")
    val type: String? = null
)