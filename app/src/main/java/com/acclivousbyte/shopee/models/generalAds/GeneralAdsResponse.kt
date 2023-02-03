package com.acclivousbyte.shopee.models.generalAds


import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class GeneralAdsResponse(
    val Data: List<GeneralAdsData>? = null
): BaseResponse()