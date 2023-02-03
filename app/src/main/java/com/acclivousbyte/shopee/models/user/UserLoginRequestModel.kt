package com.acclivousbyte.shopee.models.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLoginRequestModel(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)