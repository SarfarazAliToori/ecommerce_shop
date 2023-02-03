package com.acclivousbyte.shopee.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


open class BaseResponse {
    //    @SerialName(value = "Status")
    val Status: Int = 0

    //    @SerialName(value = "Message")
    var Message: String = ""

    val isSuccessResponse by lazy {
        Status == 200
    }
}