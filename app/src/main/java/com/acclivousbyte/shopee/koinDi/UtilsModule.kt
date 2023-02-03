package com.acclivousbyte.shopee.koinDi

import android.app.Application
import android.content.SharedPreferences
import com.acclivousbyte.shopee.data.repository.DefaultRepository
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val utilModule = module {

    single { MaterialDialogHelper() }

    single { DefaultRepository(get()) }


    single{
        getSharedPrefs(androidApplication())
    }

    single<SharedPreferences.Editor> {
        getSharedPrefs(androidApplication()).edit()
    }

}

val sharePrefModule = module {
    single {
        SharePreferenceHelper(get())
    }
}


fun getSharedPrefs(androidApplication: Application): SharedPreferences{
    return  androidApplication.getSharedPreferences("default",  android.content.Context.MODE_PRIVATE)
}

