package com.acclivousbyte.shopee.view.activties.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import java.util.*

abstract class BaseActivity<VM : ViewModel>(
    private val layoutResourceId: Int
): AppCompatActivity() {


    protected abstract val viewModel: VM

    protected open val isFullscreen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isFullscreen) setFullScreenWindow()
        setLocale()
        setContentView(layoutResourceId)


    }

    protected fun <T, LD : LiveData<T>> observe(liveData: LD, onChanged: (T) -> Unit) {
//        liveData.observe(this, Observer {
//            it?.let(onChanged)
//        })
        liveData.observe(this, androidx.lifecycle.Observer {
            it?.let(onChanged)
        })
    }


    private fun setFullScreenWindow() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    /**
     * It force the app to open in english language locale. Because we need to restrict the app to show from right to left.
     */

    private fun setLocale() {
        val locale = Locale("en")
        val config = Configuration()
        Locale.setDefault(locale)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}