package com.acclivousbyte.shopee.extensions

import android.widget.ImageView
import android.widget.ProgressBar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import timber.log.Timber

fun ImageView.loadImageWithProgress(url: String, progressBar: ProgressBar) {
    if (url.isEmpty()) {
        progressBar.gone()
        return
    }
    progressBar.visible()
    Picasso.get().load(url).into(this, object : Callback {
        override fun onSuccess() {
            progressBar.gone()
        }

        override fun onError(e: Exception?) {
            progressBar.gone()
            Timber.e("ImageLoadingError => $e")
        }
    })
}