package com.acclivousbyte.shopee.models.promotionList


import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Parcelize
//data class PromotionData(
//    val device_image: String,
//    val features: String,
//    val id: Int,
//    val old_price: Int,
//    val price: Int,
//    val short_description: String,
//    val slug: String,
//    val status: String,
//    val title: String,
//    val webImage: String
//) : Parcelable


@Serializable
data class PromotionData(
    @SerialName("device_image")
    val device_image: String?,
    @SerialName("features")
    val features: String?,
    @SerialName("id")
    val id: Int?,
    @SerialName("old_price")
    val old_price: Int?,
    @SerialName("price")
    val price: Int?,
    @SerialName("short_description")
    val short_description: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("web_image")
    val webImage: String?
) : java.io.Serializable