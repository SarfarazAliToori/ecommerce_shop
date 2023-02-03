package com.acclivousbyte.shopee.view.fragments.adsHome

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsData
import com.acclivousbyte.shopee.models.premiumAds.PremiumAdsData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class AdsHomeViewModel(private val repository: DefaultRepository) : BaseViewModel() {


    val premiumAdsMutableLiveData: MutableLiveData<Event<List<PremiumAdsData>>> = MutableLiveData()

//    val generalAdsMutableLiveData: MutableLiveData<Event<List<GeneralAdsData>>> = MutableLiveData()
//    fun generalAds() {
//        _showHideProgressDialog.value = true.wrapWithEvent()
//        viewModelScope.launch {
//            repository.generalAds().run {
//                onSuccess {
//                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
//                        _showHideProgressDialog.value = false.wrapWithEvent()
//                        generalAdsMutableLiveData.value = it.Data.wrapWithEvent()
//                    } else {
//                        Timber.e("adsHome => ${it.Message}")
//                        //Log.i("ads", "${it.Message}")
//                    }
//                }
//                onFailure {
//                    Timber.e("adsHome => ${it.message}")
//                    //Log.i("ads ==", "onFailure :${it.message}")
//                }
//            }
//        }
//    }

    fun premiumAds() {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.premiumAds().run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        premiumAdsMutableLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Log.i("premiumAds", "${it.Message}")
                    }
                }
                onFailure {
                    Log.i("premiumAds", "${it.message}")
                }
            }
        }
    }

}