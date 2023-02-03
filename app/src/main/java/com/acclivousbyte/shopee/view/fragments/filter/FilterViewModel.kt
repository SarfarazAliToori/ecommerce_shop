package com.acclivousbyte.shopee.view.fragments.filter

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.models.brands.BrandsData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.wrapWithEvent
import com.acclivousbyte.shopee.viewModel.BaseViewModel
import kotlinx.coroutines.launch

class FilterViewModel(private val repository: DefaultRepository) : BaseViewModel() {

    val filterListMutableLiveData: MutableLiveData<Event<BrandsData>> = MutableLiveData()

    fun filterData() {
        _showHideProgressDialog.value = true.wrapWithEvent()
        viewModelScope.launch {
            repository.brandsList().run {
                onSuccess {
                    _showHideProgressDialog.value = false.wrapWithEvent()
                    if (it.Status == AppUtils.VALID_STATUS_CODE && it.Data != null) {
                        filterListMutableLiveData.value = it.Data.wrapWithEvent()
                    } else {
                        Log.i("brandList", "${it.Message}")
                    }
                }
                onFailure {
                    Log.i("brandList", "${it.message}")
                }
            }
        }
    }

}