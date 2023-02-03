package com.acclivousbyte.shopee.view.fragments.base


import androidx.lifecycle.ViewModel
import org.koin.core.parameter.ParametersDefinition
import kotlin.reflect.KClass
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class MainMVVMNavigationFragment<out VM : ViewModel> (
    viewModelClass: KClass<VM>,
    viewmodelParameters: ParametersDefinition? = null
): BaseFragment(){
    protected open val viewModel: VM by viewModel(
        clazz = viewModelClass,
        parameters = viewmodelParameters
    )
}