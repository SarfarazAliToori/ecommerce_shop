package com.acclivousbyte.shopee.utils

import androidx.constraintlayout.widget.Constraints
import androidx.work.NetworkType

fun <T> T.wrapWithEvent() = Event(this)

//val internetWorkerConstraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
//    .build()