package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsCategory(
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("image_device")
    val imageDevice: String,
    @SerialName("image_device_url")
    val imageDeviceUrl: String,
    @SerialName("image_web")
    val imageWeb: String,
    @SerialName("name")
    val name: String,
    @SerialName("parent_id")
    val parentId: Int
)