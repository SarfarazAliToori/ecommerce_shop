package com.acclivousbyte.shopee.models.productsList


import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val Data: List<ProductsListData>? = null
): BaseResponse()