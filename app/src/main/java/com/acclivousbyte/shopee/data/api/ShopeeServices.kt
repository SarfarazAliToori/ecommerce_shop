package com.acclivousbyte.shopee.data.api

import com.acclivousbyte.shopee.models.BaseResponse
import com.acclivousbyte.shopee.models.brands.BrandListResponse
import com.acclivousbyte.shopee.models.brands.Manufacturer
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsResponse
import com.acclivousbyte.shopee.models.placeOrder.PlaceOrderResponse
import com.acclivousbyte.shopee.models.placeOrder.PlaceOrderRequest
import com.acclivousbyte.shopee.models.premiumAds.PremiumAdsResponse
import com.acclivousbyte.shopee.models.productsList.ProductListResponse
import com.acclivousbyte.shopee.models.promotionList.PromotionsResponse
import com.acclivousbyte.shopee.models.updateProfile.UpdateUserProfileRequest
import com.acclivousbyte.shopee.models.updateProfile.UpdateUserProfileResponse
import com.acclivousbyte.shopee.models.user.UserLoginRequestModel
import com.acclivousbyte.shopee.models.user.UserLoginResponse
import com.acclivousbyte.shopee.models.user.UserRegisterResponse
import com.acclivousbyte.shopee.models.user.UserRegisterRequestModel
import com.acclivousbyte.shopee.models.userOrders.UserOrderRequest
import com.acclivousbyte.shopee.models.userOrders.UserOrderResponse
import com.acclivousbyte.shopee.models.userOrders.UserOrdersData
import retrofit2.http.*

interface ShopeeServices {


    @POST("register")
    suspend fun userRegistration(@Body body: UserRegisterRequestModel): UserRegisterResponse

    @POST("login")
    suspend fun userLogIn(@Body userLoginModel: UserLoginRequestModel): UserLoginResponse

    @GET("promotions")
    suspend fun promotionListItems(): PromotionsResponse

    @GET("ads/general")
    suspend fun generalAds(): GeneralAdsResponse

    @GET("ads/premium")
    suspend fun premiumAds(): PremiumAdsResponse

    @GET("products")
    suspend fun productsList(): ProductListResponse

    /**
     * Query params for Filters in Get Products API:
    query, category_id, manufacturer_id, brand_id, model_id, year_id, page, per_page
     */
    @GET("products")
    suspend fun filterBrandsList(
        @Query("category_id") categoryId: Int,
        @Query("manufacturer_id") manufacturerId: Int,
        @Query("brand_id") brandId: Int,
        @Query("model_id") modelId: Int,
        @Query("year_id") yearId: Int,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): ProductListResponse


    @GET("products")
    suspend fun filterProductsListUsingQueryMap(
        @QueryMap queryOptions: Map<String, Int>
    ): ProductListResponse

    // user one of the above for query parameter.
    //////////////

    @GET("seed_data")
    suspend fun brandsList(): BrandListResponse

    @POST("users/order/post")
    suspend fun userCreateOrder(
        @Header("Authorization") authToken: String,
        @Body placeOrder: PlaceOrderRequest
    ): PlaceOrderResponse

    @POST("users/orders")
    suspend fun userOrders(
        @Header("Authorization") authToken: String,
        @Body userId: UserOrderRequest
    ): UserOrderResponse

    @POST("users/editprofile")
    suspend fun updateUserProfile(
        @Header("Authorization") authToken: String,
        @Body userInfo: UpdateUserProfileRequest
    ): UpdateUserProfileResponse

}