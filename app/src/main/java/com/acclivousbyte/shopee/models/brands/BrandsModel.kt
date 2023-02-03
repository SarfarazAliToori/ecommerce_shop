package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsModel(
    @SerialName("brands_id")
    val brands_id: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)