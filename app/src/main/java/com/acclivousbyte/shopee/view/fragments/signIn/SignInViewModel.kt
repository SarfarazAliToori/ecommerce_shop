package com.acclivousbyte.shopee.view.fragments.signIn

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.user.UserLoginData
import com.acclivousbyte.shopee.models.user.UserLoginRequestModel
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel(
    private val repository: DefaultRepository,
    private val sharePreferenceHelper: SharePreferenceHelper
) : BaseViewModel() {

    val userLogInLiveData: MutableLiveData<Event<UserLoginData>> = MutableLiveData()

    fun userLogIn(userLoginRequestModel: UserLoginRequestModel) {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.userLogIn(userLoginRequestModel).run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        userLogInLiveData.value = it.Data.wrapWithEvent()
                        sharePreferenceHelper.storeUserData(it.Data)
//                        sharePreferenceHelper.storeUserLoggedIn(true)
//                        sharePreferenceHelper.storeUsersToken(it.Data.token)

                        Timber.e("userLogin => ${it.Data}")
                    } else {
                        Timber.e("error=> ${it.Message}")
                    }
                }
                _showHideProgressDialog.value = false.wrapWithEvent()

                onFailure {
                    Timber.e("error=> ${it.message}")
                    Log.i("error", "${it.message}")
                }
            }
        }
    }
}