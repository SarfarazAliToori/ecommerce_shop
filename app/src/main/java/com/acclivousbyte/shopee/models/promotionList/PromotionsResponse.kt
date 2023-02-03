package com.acclivousbyte.shopee.models.promotionList

import com.acclivousbyte.shopee.models.BaseResponse
import com.acclivousbyte.shopee.models.user.UserLoginData

data class PromotionsResponse(
    val Data: PromotionList? = null
): BaseResponse()
