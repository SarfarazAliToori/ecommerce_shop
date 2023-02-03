package com.acclivousbyte.shopee.view.fragments.order.orderListing

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsData
import com.acclivousbyte.shopee.models.userOrders.UserOrderRequest
import com.acclivousbyte.shopee.models.userOrders.UserOrdersData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class OrderListingViewModel(private val repository: DefaultRepository) : BaseViewModel() {

    val generalAdsMutableLiveData: MutableLiveData<Event<List<GeneralAdsData>>> = MutableLiveData()
    val userOrderMutableLiveData: MutableLiveData<Event<List<UserOrdersData>>> = MutableLiveData()

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

    fun userOrders(authToken: String, userId: UserOrderRequest) {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.userOrders(authToken, userId).run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        userOrderMutableLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Log.i("userOrder", it.Message)
                    }
                }

                onFailure {
                    Log.i("userOrderE", "${it.message}")
                }
            }
        }
    }

}