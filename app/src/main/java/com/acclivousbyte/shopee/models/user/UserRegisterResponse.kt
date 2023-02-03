package com.acclivousbyte.shopee.models.user

import com.acclivousbyte.shopee.models.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterResponse(
    val Data: UserLoginData? = null
) : BaseResponse()
