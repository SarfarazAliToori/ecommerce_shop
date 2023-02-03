package com.acclivousbyte.shopee.models.premiumAds


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PremiumAdsData(
    @SerialName("category_id")
    val categoryId: String,
    @SerialName("id")
    val id: Int,
    @SerialName("images")
    val images: List<String>,
    @SerialName("url")
    val url: String
)