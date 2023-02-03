package com.acclivousbyte.shopee.models.generalAds


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeneralAdsData(
    @SerialName("category_id")
    val categoryId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("images")
    val images: List<String>,
    @SerialName("url")
    val url: String
): java.io.Serializable