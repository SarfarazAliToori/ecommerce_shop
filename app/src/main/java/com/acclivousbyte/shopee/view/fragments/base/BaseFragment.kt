package com.acclivousbyte.shopee.view.fragments.base

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.acclivousbyte.shopee.koinDi.BaseApplication
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class BaseFragment : Fragment() {


    val myCustomApp by lazy {
        requireContext().applicationContext as BaseApplication
    }

    private var mPreviousView: View? = null
    private var mModule: Module? = null

//    @LayoutRes
//    abstract fun getLayoutResId(): Int

//    abstract fun inOnCreateView(
//        mRootView: ViewGroup, savedInstanceState: Bundle?
//    )

    open fun themeInflater(baseInflater: LayoutInflater): LayoutInflater? = null

    open fun initializeView(view: View) {}

    open fun registerModule(): Module? = null

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        mPreviousView?.let { return it }
//        val newInflater = themeInflater(inflater) ?: inflater
//        val mView = newInflater.inflate(getLayoutResId(), container, false)
//        initializeView(mView)
//        val module = registerModule()
//        if (module != null)
//            runCatching { loadKoinModules(module).also { mModule = module } }
//        inOnCreateView(mView as ViewGroup, savedInstanceState)
//        return mView.also {
//            mPreviousView = it
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) {
        liveData.observe(this, Observer {
            it?.let(onChanged)
        })
    }

    suspend fun customDelay(delayValue: Long) = withContext(Dispatchers.IO) {
        delay(delayValue)
    }

    override fun onDestroy() {
        super.onDestroy()
        mModule?.let { unloadKoinModules(it) }
    }

    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]+)\$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun isValidPhoneNumber(phone: String?): Boolean {
        phone?.let {
//            val phonePattern = "^\\+[0-9]{10,13}\$"
            val phonePattern = "^\\[0-9]{10,13}\$"
            val phoneNumberMatcher = Regex(phonePattern)
            return phoneNumberMatcher.find(phone) != null
        } ?: return false
    }

    fun isValidPhoneNum(phone: String): Boolean {
        return if (phone.trim { it <= ' ' } != "" && phone.length >= 10) {
            Patterns.PHONE.matcher(phone).matches()
        } else false
    }

    fun showAlert(title: String, message: String, yesButton: String, noButton:String? = null, onPositiveClick:() -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(noButton) { _q, _a ->
                _q.dismiss()
            }
            .setPositiveButton(yesButton) { _, _ ->
                onPositiveClick.invoke()
            }
            .show()
    }

    fun progress(progressDialog: AlertDialog, isShow: Boolean) {
        progressDialog.setTitle("Loading...")
        progressDialog.setCanceledOnTouchOutside(false)
        if (isShow) progressDialog.show() else progressDialog.dismiss()
    }


}