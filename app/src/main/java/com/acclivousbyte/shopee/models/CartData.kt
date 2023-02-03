package com.acclivousbyte.shopee.models

import kotlinx.serialization.Serializable

@Serializable
data class CartData(
    // data from promotions and products list
    val productsId: String? = null,
    val productsTitle: String? = null,
    val productsDescription: String? = null,
    val productsFeatures: String? = null,
    val productsPrice: String? = null,
    val productImage: String? = null,
    val productType: String? = null,
    var productQuantity: Int = 1
):java.io.Serializable