package com.acclivousbyte.shopee.models.updateProfile

import com.acclivousbyte.shopee.models.BaseResponse
import com.acclivousbyte.shopee.models.user.UserLoginData

data class UpdateUserProfileResponse (
    val Data: UserLoginData? = null
): BaseResponse()
