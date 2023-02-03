package com.acclivousbyte.shopee.models.placeOrder

import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class PlaceOrderResponse(
    val Data: OrderSummaryData
): BaseResponse()
