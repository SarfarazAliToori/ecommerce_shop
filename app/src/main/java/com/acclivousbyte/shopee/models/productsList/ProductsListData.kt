package com.acclivousbyte.shopee.models.productsList


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsListData(
    @SerialName("category_id")
    val category_id: Int?,
    @SerialName("created_at")
    val created_at: String?,
    @SerialName("deleted")
    val deleted: Int?,
    @SerialName("deleted_at")
    val deleted_at: String?,
    @SerialName("description")
    val description: String?,
    @SerialName("draft")
    val draft: Int?,
    @SerialName("generic")
    val generic: Int?,
    @SerialName("id")
    val id: Int?,
    @SerialName("image_web")
    val image_web: String?,
    @SerialName("logo")
    val logo: String?,
    @SerialName("manufacturer_id")
    val manufacturer_id: Int?,
    @SerialName("rate")
    val rate: Int?,
    @SerialName("searchable")
    val searchable: Int?,
    @SerialName("size")
    val size: String?,
    @SerialName("slug")
    val slug: String?,
    @SerialName("status")
    val status: Int?,
    @SerialName("title")
    val title: String?,
    @SerialName("type")
    val type: String?,
    @SerialName("updated_at")
    val updated_at: String?,
    @SerialName("why")
    val why: String?
): java.io.Serializable