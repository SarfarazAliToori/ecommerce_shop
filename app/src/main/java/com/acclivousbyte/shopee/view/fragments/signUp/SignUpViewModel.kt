package com.acclivousbyte.shopee.view.fragments.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.user.UserLoginData
import com.acclivousbyte.shopee.models.user.UserRegisterRequestModel
import com.acclivousbyte.shopee.models.user.UserRegisterResponse
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class SignUpViewModel(
    private val repository: DefaultRepository,
    private val sharePreferenceHelper: SharePreferenceHelper
) : BaseViewModel() {

    val userRegisterationLiveData: MutableLiveData<Event<UserLoginData>> = MutableLiveData()
    val error: MutableLiveData<Event<String>> = MutableLiveData()

    fun userRegistration(userRegisterRequestModel: UserRegisterRequestModel) {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.userRegistration(userRegisterRequestModel).run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        userRegisterationLiveData.value = it.Data.wrapWithEvent()
                        sharePreferenceHelper.storeUserData(it.Data)

                    } else {
                        Timber.e("error=> ${it.Message}")
                        error.postValue(it.Message.wrapWithEvent())
                    }
                }

                onFailure {
                    Timber.e("error=> ${it.message}")
                    error.postValue(AppUtils.GENERIC_ERROR_MESSAGE.wrapWithEvent())
                }
            }
        }
    }
}