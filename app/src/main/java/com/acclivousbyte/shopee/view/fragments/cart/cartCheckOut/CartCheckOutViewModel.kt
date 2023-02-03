package com.acclivousbyte.shopee.view.fragments.cart.cartCheckOut

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsData
import com.acclivousbyte.shopee.models.placeOrder.OrderSummaryData
import com.acclivousbyte.shopee.models.placeOrder.PlaceOrderRequest
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class CartCheckOutViewModel(private val repository: DefaultRepository) : BaseViewModel() {

    val generalAdsMutableLiveData: MutableLiveData<Event<List<GeneralAdsData>>> = MutableLiveData()
    val orderSummaryLiveData: MutableLiveData<Event<OrderSummaryData>> = MutableLiveData()

    fun generalAds() {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.generalAds().run {
                onSuccess {
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        _showHideProgressDialog.value = false.wrapWithEvent()
                        generalAdsMutableLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Timber.e("adsHome => ${it.Message}")
                        //Log.i("ads", "${it.Message}")
                    }
                }
                onFailure {
                    Timber.e("adsHome => ${it.message}")
                    //Log.i("ads ==", "onFailure :${it.message}")
                }
            }
        }
    }

    fun createUserOrder(authToken: String, placeOrder: PlaceOrderRequest) {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.userCreateOrder(authToken, placeOrder).run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        orderSummaryLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Log.i("order1", "${it.Message}")
                    }
                }
                onFailure {
                    Log.i("order", "${it.message}")
                }
            }
        }
    }
}