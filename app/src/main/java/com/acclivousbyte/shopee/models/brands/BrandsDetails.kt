package com.acclivousbyte.shopee.models.brands


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandsDetails(
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("image_web")
    val imageWeb: String?,
    @SerialName("images")
    val images: List<String> = arrayListOf(),
    @SerialName("logo")
    val logo: String?,
    @SerialName("logo_url")
    val logo_url: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("web_url")
    val web_url: String?,
    @SerialName("youtube_url")
    val youtube_url: String?
): java.io.Serializable