package com.acclivousbyte.shopee.data.repository

import com.acclivousbyte.shopee.data.api.ShopeeServices
import com.acclivousbyte.shopee.models.placeOrder.PlaceOrderRequest
import com.acclivousbyte.shopee.models.updateProfile.UpdateUserProfileRequest
import com.acclivousbyte.shopee.models.user.UserLoginRequestModel
import com.acclivousbyte.shopee.models.user.UserRegisterRequestModel
import com.acclivousbyte.shopee.models.userOrders.UserOrderRequest
import com.acclivousbyte.shopee.utils.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultRepository(private val shopeeServies: ShopeeServices) {

    private val dispatcher = Dispatchers.IO

    suspend fun userRegistration(userRegisterModel: UserRegisterRequestModel) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.userRegistration(userRegisterModel))
        }
    }

    suspend fun userLogIn(userLoginModel: UserLoginRequestModel) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.userLogIn(userLoginModel))
        }
    }

    suspend fun promotionListItems() = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.promotionListItems())
        }
    }

    suspend fun generalAds() = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.generalAds())
        }
    }

    suspend fun premiumAds() = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.premiumAds())
        }
    }

    suspend fun productsList() = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.productsList())
        }
    }

    suspend fun brandsList() = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.brandsList())
        }
    }

    suspend fun userCreateOrder(authToken: String, placeOrder: PlaceOrderRequest) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.userCreateOrder(authToken,placeOrder))
        }
    }

    suspend fun userOrders(authToken: String, userId: UserOrderRequest) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.userOrders(authToken,userId))
        }
    }

    suspend fun updateUserProfile(authToken: String, userInfo: UpdateUserProfileRequest) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.updateUserProfile(authToken, userInfo))
        }
    }

    suspend fun filterProductsListUsingQueryMap(queryOptions: Map<String, Int>) = withContext(dispatcher) {
        safeApiCall {
            Result.success(shopeeServies.filterProductsListUsingQueryMap(queryOptions))
        }
    }

}