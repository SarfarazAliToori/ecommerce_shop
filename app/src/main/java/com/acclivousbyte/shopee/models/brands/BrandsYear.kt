package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsYear(
    @SerialName("id")
    val id: Int,
    @SerialName("models_id")
    val models_id: Int,
    @SerialName("year")
    val year: Int
)