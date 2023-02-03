package com.acclivousbyte.shopee.koinDi

import com.acclivousbyte.shopee.view.fragments.adsHome.AdsHomeViewModel
import com.acclivousbyte.shopee.view.fragments.brands.BrandsViewModel
import com.acclivousbyte.shopee.view.fragments.cart.CartViewModel
import com.acclivousbyte.shopee.view.fragments.cart.cartCheckOut.CartCheckOutViewModel
import com.acclivousbyte.shopee.view.fragments.contactUs.ContactUsViewModel
import com.acclivousbyte.shopee.view.fragments.filter.FilterViewModel
import com.acclivousbyte.shopee.view.fragments.order.orderListing.OrderListingViewModel
import com.acclivousbyte.shopee.view.fragments.order.orderSummary.OrderSummaryViewModel
import com.acclivousbyte.shopee.view.fragments.products.ProductsViewModel
import com.acclivousbyte.shopee.view.fragments.profile.ProfileViewModel
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionsViewModel
import com.acclivousbyte.shopee.view.fragments.settings.SettingsViewModel
import com.acclivousbyte.shopee.view.fragments.signIn.SignInViewModel
import com.acclivousbyte.shopee.view.fragments.signUp.SignUpViewModel
import com.acclivousbyte.shopee.view.fragments.wishlist.WishListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel {
     AdsHomeViewModel(get())
    }

    viewModel {
        SignUpViewModel(get(), get())
    }

    viewModel {
        SignInViewModel(get(), get())
    }

    viewModel {
        PromotionsViewModel(get())
    }

    viewModel {
        BrandsViewModel(get())
    }

    viewModel {
        ProductsViewModel(get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        WishListViewModel(get())
    }

    viewModel {
        CartViewModel(get())
    }

    viewModel {
        CartCheckOutViewModel(get())
    }

    viewModel {
        OrderSummaryViewModel(get())
    }

    viewModel {
        ContactUsViewModel(get())
    }

    viewModel {
        OrderListingViewModel(get())
    }

    viewModel {
        ProfileViewModel(get())
    }

    viewModel {
        FilterViewModel(get())
    }
}