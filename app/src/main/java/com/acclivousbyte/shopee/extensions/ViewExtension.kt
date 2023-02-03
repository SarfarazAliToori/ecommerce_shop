package com.acclivousbyte.shopee.extensions

import android.view.View


fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun View?.visible(visible: Boolean) {
    this?.visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.gone(gone: Boolean) {
    this?.visibility = if (gone) View.GONE else View.VISIBLE
}