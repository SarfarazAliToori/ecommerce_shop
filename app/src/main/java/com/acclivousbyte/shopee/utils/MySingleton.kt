package com.acclivousbyte.shopee.utils

import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.placeOrder.Item

object MySingleton {

    var showHideCart: Boolean = false
    var showHideFilter: Boolean = false

    var cartDataList: MutableList<CartData> = mutableListOf()
    var listProductId: MutableList<String> = mutableListOf()
    val listItems: MutableList<Item> = mutableListOf()
}