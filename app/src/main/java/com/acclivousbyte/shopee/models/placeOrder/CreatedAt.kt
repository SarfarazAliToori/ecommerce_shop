package com.acclivousbyte.shopee.models.placeOrder


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedAt(
    @SerialName("date")
    val date: String? = null,
    @SerialName("timezone")
    val timezone: String? = null,
    @SerialName("timezone_type")
    val timezone_type: Int?
)