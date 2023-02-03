package com.acclivousbyte.shopee.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.acclivousbyte.shopee.utils.Event
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.BaseFragment
import com.afollestad.materialdialogs.MaterialDialog

fun BaseFragment.setupProgressDialog(
    showHideProgress: LiveData<Event<Boolean>>,
    dialogHelper: MaterialDialogHelper
) {
    var mDialog: MaterialDialog? = null
    showHideProgress.observe(this, Observer {
        if (!it.consumed) it.consume()?.let { flag ->
            if (flag)
                mDialog?.show() ?: dialogHelper.showSimpleProgress(requireContext())
                    .also { dialog ->
                        mDialog = dialog
                    }
            else mDialog?.dismiss()
        }
    })
}