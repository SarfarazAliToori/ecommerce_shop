package com.acclivousbyte.shopee.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.productsList.ProductsListData
import com.acclivousbyte.shopee.models.promotionList.PromotionData
import com.acclivousbyte.shopee.models.user.UserLoginData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class SharePreferenceHelper(private val sharedPreferences: SharedPreferences) {

    private val dispatcher = Dispatchers.Default

    companion object {

        private const val USER_SHARED_PREFERENCE = "shopee_app_preferences"
        private const val USER_DATA = "user_log_in_data"
        private const val STORE_USER_TOKEN = "user_log_in_token"
        private const val IS_USER_LOGGED_IN = "is_user_logged_in"
        private const val IS_EDITABLE = "is_editable"
        private const val STORE_PRODUCT_ID = "store_product_id"
        private const val PRODUCT_LIST_DATA = "product_list_data"
        private const val LIST_OF_PRODUCTS = "list_of_products"
        private const val PRODUCTS_ID_NEW = "list_of_products_new"
        private const val ADD_TO_CART_PRODUCTS = "add_to_cart_products"
        private const val ADD_TO_CART_PRODUCTS_ID = "add_to_cart_products_id"
        private const val CART_DATA = "cart_data"
        private const val PAYMENT_CARD_DATA = "payment_card_data"

        private var INSTANCE: SharePreferenceHelper? = null

        fun getInstance(context: Context): SharePreferenceHelper {

            val sharedPreference =
                context.getSharedPreferences(USER_SHARED_PREFERENCE, Context.MODE_PRIVATE)
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharePreferenceHelper(sharedPreference).also { INSTANCE = it }
            }
        }
    }


    private suspend fun put(key: String, value: String) = withContext(dispatcher) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    private suspend fun put(key: String, value: Boolean) = withContext(dispatcher) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    private suspend fun put(key: String, value: Int) = withContext(dispatcher) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    private suspend fun <T> put(key: String, `object`: T) {
        val jsonString = GsonBuilder().create().toJson(`object`)
        put(key, jsonString)
    }

    private inline fun <reified T> getObject(key: String): T? {
        val value = this.sharedPreferences.getString(key, "")
        return try {
            GsonBuilder().create().fromJson(value, T::class.java)
        } catch (e: Exception) {
            Timber.e("try_catch_error => $e")
            null
        }
    }

    suspend fun storeUserData(userLoginData: UserLoginData) {
        put(USER_DATA, userLoginData)
    }

    fun getUserData(): UserLoginData? {
        return getObject<UserLoginData>(USER_DATA)
    }

    suspend fun storeProductsToWishlist(productListData: ProductsListData) {
        put(PRODUCT_LIST_DATA, productListData)
    }

    fun getProductListData(): ProductsListData? {
        return getObject<ProductsListData>(PRODUCT_LIST_DATA)
    }


    fun removeUserData() {
        sharedPreferences.edit().remove(USER_DATA).apply()
    }

    suspend fun storeUsersToken(token: String) {
        put(STORE_USER_TOKEN, token)
    }

    fun getUsersToken(): String? {
        return sharedPreferences.getString(STORE_USER_TOKEN, "")
    }


    suspend fun storeUserLoggedIn(isLoggedIn: Boolean) {
        put(IS_USER_LOGGED_IN, isLoggedIn)
    }

    fun getIsUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_USER_LOGGED_IN, false)
    }

    fun getIsEditable(): Boolean {
        return sharedPreferences.getBoolean(IS_EDITABLE, false)
    }

    suspend fun saveIsEditable(isEditable: Boolean) {
        put(IS_EDITABLE, isEditable)
    }

    suspend fun saveProductId(productId: String) {
        put(STORE_PRODUCT_ID, productId)
    }

    fun getProductId(): String? {
        return sharedPreferences.getString(STORE_PRODUCT_ID, null)
    }
    suspend fun clear() {
        val isEditable = getIsEditable()
        sharedPreferences.edit { clear() }
        saveIsEditable(isEditable)
    }



    fun setWishlistProducts(productData: MutableList<ProductsListData>) {
        val gson = Gson()
//        val dd: MutableList<ProductsListData> = getListOfProducts()
//        dd.addAll(productData)
        val json = gson.toJson(productData)
        set(LIST_OF_PRODUCTS, json)
    }

    fun getWishlistProducts(): MutableList<ProductsListData> {
        var arrayItems = mutableListOf<ProductsListData>()
        val serializedObject = sharedPreferences.getString(LIST_OF_PRODUCTS, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<ProductsListData?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<ProductsListData>>(serializedObject, type)
        }

        return arrayItems
    }

    fun saveProductIdNew(productId: MutableList<String>) {
        val gson = Gson()
        val add: MutableList<String> = getProductIdNew()
        add.addAll(productId)
        val json = gson.toJson(add)
        set(PRODUCTS_ID_NEW, json)
    }

    fun getProductIdNew(): MutableList<String> {
        var arrayItems = mutableListOf<String>()
        val serializedObject = sharedPreferences.getString(PRODUCTS_ID_NEW, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<String?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<String>>(serializedObject, type)
        }
        return arrayItems
    }

    fun setAddToCartProductFromPromotions(productData: MutableList<PromotionData>) {
        val gson = Gson()
        val json = gson.toJson(productData)
        set(ADD_TO_CART_PRODUCTS, json)
    }

    fun getAddToCartProductFromPromotions(): MutableList<PromotionData> {
        var arrayItems = mutableListOf<PromotionData>()
        val serializedObject = sharedPreferences.getString(ADD_TO_CART_PRODUCTS, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<PromotionData?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<PromotionData>>(serializedObject, type)
        }
        return arrayItems
    }

    fun setAddToCartProductId(productId: MutableList<String>) {
        val gson = Gson()
        val add: MutableList<String> = getProductIdNew()
        add.addAll(productId)
        val json = gson.toJson(add)
        set(ADD_TO_CART_PRODUCTS_ID, json)
    }

    fun getAddToCartProductId(): MutableList<String> {
        var arrayItems = mutableListOf<String>()
        val serializedObject = sharedPreferences.getString(ADD_TO_CART_PRODUCTS_ID, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<String?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<String>>(serializedObject, type)
        }
        return arrayItems
    }

    fun setCartData(cartData: MutableList<CartData>) {
        val gson = Gson()
        val json = gson.toJson(cartData)
        set(CART_DATA, json)
    }

    fun getCartData(): MutableList<CartData> {
        var arrayItems = mutableListOf<CartData>()
        val serializedObject = sharedPreferences.getString(CART_DATA, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<CartData?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<CartData>>(serializedObject, type)
        }

        return arrayItems
    }

    fun set(key: String?, value: String?) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun setPaymentCardData(paymentParam: String?) {
        val gson = Gson()
        val json = gson.toJson(paymentParam)
        set(PAYMENT_CARD_DATA, json)
    }

    fun getPaymentCardData(): MutableList<String> {
        var arrayItems = mutableListOf<String>()
        val serializedObject = sharedPreferences.getString(PAYMENT_CARD_DATA, null)
        if (serializedObject != null) {
            val gson = Gson()
            val type = object : TypeToken<MutableList<String?>?>() {}.type
            arrayItems = gson.fromJson<MutableList<String>>(serializedObject, type)
        }
        return arrayItems
    }

}