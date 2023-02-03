package com.acclivousbyte.shopee.models.promotionList


import android.os.Parcelable
//import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
data class PromotionList(
    val current_page: Int,
    val data: List<PromotionData>,
    val first_page_url: String,
    val from: Int,
    val last_page: Int,
    val last_page_url: String,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: String,
    val to: Int,
    val total: Int
): Parcelable

//@Serializable
//data class PromotionList(
//    @SerialName("current_page")
//    val currentPage: Int,
//    @SerialName("data")
//    val `data`: List<PromotionData>,
//    @SerialName("first_page_url")
//    val firstPageUrl: String,
//    @SerialName("from")
//    val from: Int,
//    @SerialName("last_page")
//    val lastPage: Int,
//    @SerialName("last_page_url")
//    val lastPageUrl: String,
//    @SerialName("next_page_url")
//    val nextPageUrl: String,
//    @SerialName("path")
//    val path: String,
//    @SerialName("per_page")
//    val perPage: Int,
//    @SerialName("prev_page_url")
//    val prevPageUrl: String,
//    @SerialName("to")
//    val to: Int,
//    @SerialName("total")
//    val total: Int
//)