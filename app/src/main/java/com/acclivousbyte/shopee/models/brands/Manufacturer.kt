package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Manufacturer(
    @SerialName("categories_ids")
    val categories_ids: List<Int>,
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("image_device")
    val image_device: String,
    @SerialName("image_web")
    val image_web: String,
    @SerialName("images")
    val images: List<String>,
    @SerialName("name")
    val name: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("web_url")
    val web_url: String,
    @SerialName("youtube_url")
    val youtube_url: String
)