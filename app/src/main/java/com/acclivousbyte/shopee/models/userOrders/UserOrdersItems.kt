package com.acclivousbyte.shopee.models.userOrders


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserOrdersItems(
    @SerialName("additional_images")
    val additional_images: List<String>?,
    @SerialName("description")
    val description: String?,
    @SerialName("device_image")
    val device_image: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("old_price")
    val old_price: Int?,
    @SerialName("price")
    val price: Int?,
    @SerialName("promotion_products")
    val promotion_products: List<PromotionProduct>?,
    @SerialName("quantity")
    val quantity: Int?,
    @SerialName("sub_category")
    val sub_category: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("type")
    val type: String?
)