package com.acclivousbyte.shopee.view.fragments.products

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentProductsDetailBinding
import com.acclivousbyte.shopee.extensions.mutedColor
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.promotionList.PromotionData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.activties.base.MainActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsDetailFragment : MainMVVMNavigationFragment<ProductsViewModel>(ProductsViewModel::class) {

    private lateinit var binding: FragmentProductsDetailBinding
    private val productsViewModel: ProductsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val args by navArgs<ProductsDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsDetailBinding.inflate(inflater, container, false)

       loadProductsDetails()

        return binding.root
    }

    private fun loadProductsDetails() {

        binding.fragmentProductsDetailTitle.text = args.productListData.title

        binding.fragmentProductsDetailShortDescription.text = args.productListData.description

        binding.fragmentProductsDetailWhyDetail.text = args.productListData.why

        binding.fragmentPromotionsDetailNewPriceTv.text = let {
            SpannableStringBuilder().color(requireContext().mutedColor(R.color.red)){append("RS, ")}.append("${args.productListData.rate}")
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(args.productListData.logo, ScaleTypes.FIT))

        val slider = binding.fragmentProductsDetailImageSlider
        slider.setImageList(imageList)

        observe(productsViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentProductsDetailAdsIv.promotionsBannerIv)

                binding.fragmentProductsDetailAdsIv.promotionsBannerIv.setOnClickListener {
                    val action = ProductsDetailFragmentDirections.actionProductsDetailFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        productsViewModel.generalAds()
        setupProgressDialog(productsViewModel.showHideProgressDialog, dialogHelper)

        binding.fragmentProductsDetailAddToWishlist.setOnClickListener {
            if (sharePreferenceHelper.getUserData() != null) {
                val id = args.productListData.id.toString()

                val isProductIdAvailable = sharePreferenceHelper.getProductIdNew().contains(id)

                if (isProductIdAvailable) {
                    showAlert("Alert !", "Already available in wishlist!","Ok"){}
                } else {
                    showAlert("Alert !", "You want to add product to wishlist!", "Yes", "No"){
                        addProductToWishlist()
                    }
                }

            } else {
                showAlert("Alert !", "Please login first!","Yes", "No"){
                    AppUtils.isFromProductDetail = true
                    findNavController().navigate(R.id.action_productsDetailFragment_to_signInFragment)
                }
            }

        }

        //add to cart click
        binding.fragmentProductsDetailAddToCart.setOnClickListener {
            checkUserAndAddProductsToCart()
        }


    }



    private fun addProductToWishlist() {
        viewModel.viewModelScope.launch {
            // Save Product Id for wishlist.
            val id = args.productListData.id.toString()
            val idList = mutableListOf<String>()
            idList.add(id)
            sharePreferenceHelper.saveProductIdNew(idList)

            //save products in sharePref for wishlist
            val data = args.productListData
            val list = sharePreferenceHelper.getWishlistProducts()
            list.add(data)
            sharePreferenceHelper.setWishlistProducts(list)
        }
    }

    private fun checkUserAndAddProductsToCart() {

        val id = args.productListData.id.toString()

        val isProductId = MySingleton.listProductId.contains(id)

        if (isProductId) {
            showAlert("Alert !", "Already Available in Cart!\nYou can increase or decrease the quantity from cart.","Ok"){}

        } else {
            showAlert("Alert !", "You want to add product to Cart!", "Yes", "No"){
                MySingleton.listProductId.add(args.productListData.id.toString())
                addToCartProducts()
                // we want to increment cart count, so that's why I want to recreate the activity.
                val homeActivity = requireActivity() as HomeActivity
                homeActivity.recreate()
            }
        }

    }

    private fun addToCartProducts() {
        AppUtils.showHideCartBadge = true
        AppUtils.badgeCounter++

        val cartData = CartData(
            args.productListData.id.toString(),
            args.productListData.title,
            args.productListData.description,
            args.productListData.why,
            args.productListData.rate.toString(),
            args.productListData.logo,
            "product",
        )

        MySingleton.cartDataList.add(cartData)

    }

}
