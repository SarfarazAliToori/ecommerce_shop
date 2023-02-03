package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsData(
    @SerialName("brands")
    val brands: List<BrandsDetails>,
    @SerialName("categories")
    val categories: List<BrandsCategory>,
    @SerialName("Manufacturers")
    val Manufacturers: List<Manufacturer>,
    @SerialName("models")
    val models: List<BrandsModel>,
    @SerialName("years")
    val years: List<BrandsYear>
)