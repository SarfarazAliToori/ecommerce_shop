package com.acclivousbyte.shopee.models.premiumAds


import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PremiumAdsResponse(
    @SerialName("Data")
    val Data: List<PremiumAdsData>? = null
): BaseResponse()