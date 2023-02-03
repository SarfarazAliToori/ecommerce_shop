package com.acclivousbyte.shopee.models.userOrders

import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserOrderResponse(
    @SerialName("Data")
    val Data: List<UserOrdersData>? = null
): BaseResponse()

