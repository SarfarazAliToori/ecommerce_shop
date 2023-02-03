package com.acclivousbyte.shopee.models.user

import com.acclivousbyte.shopee.models.BaseResponse


data class UserLoginResponse(
    val Data: UserLoginData? = null
) : BaseResponse()