package com.acclivousbyte.shopee.utils

import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.placeOrder.Item

class AppUtils {

    companion object {
        var showHideCartBadge: Boolean = false
        var showHideDrawerItems: Boolean = false
        var isFromCartFragment: Boolean = false
        var isFromProductDetail: Boolean = false
        var filterCheck: Boolean = false
        var checkPosition: Int = -1

        /**
         * Query params for Filters in Get Products API:
        query, category_id, manufacturer_id, brand_id, model_id, year_id, page, per_page
         */

        var category_id: Int = 0
        var manufacturer_id: Int = 0
        var brand_id: Int = 0
        var model_id: Int = 0
        var year_id: Int = 0
        var isFromFilter: Boolean = false

        val GENERIC_ERROR_MESSAGE =
            "Server is not responding properly please try again later"

        const val VALID_STATUS_CODE = 200

        var cartDataList: MutableList<CartData> = mutableListOf()
        var listProductId: MutableList<String> = mutableListOf()
        var productQuantityG: Int = 1
        var badgeCounter: Int = 0
        var GTotalPrice: Int = 0
    }

}