package com.acclivousbyte.shopee.models.brands


import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandListResponse(
    val Data: BrandsData? = null
): BaseResponse()