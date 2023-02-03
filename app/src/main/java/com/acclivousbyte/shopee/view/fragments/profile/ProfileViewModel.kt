package com.acclivousbyte.shopee.view.fragments.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsData
import com.acclivousbyte.shopee.models.updateProfile.UpdateUserProfileRequest
import com.acclivousbyte.shopee.models.user.UserLoginData
import com.acclivousbyte.shopee.models.userOrders.UserOrdersData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class ProfileViewModel(private val repository: DefaultRepository) : BaseViewModel() {


    val generalAdsMutableLiveData: MutableLiveData<Event<List<GeneralAdsData>>> = MutableLiveData()
    val userUpdateResponseLiveData: MutableLiveData<Event<UserLoginData>> = MutableLiveData()

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

    fun updateUserProfile(authToken: String, userInfo: UpdateUserProfileRequest) {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.updateUserProfile(authToken, userInfo).run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        userUpdateResponseLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Log.i("updateProfile", "${it.Message}")
                    }
                }

                onFailure {
                    Log.i("updateProfileE", "${it.message}")
                }
            }
        }
    }

}