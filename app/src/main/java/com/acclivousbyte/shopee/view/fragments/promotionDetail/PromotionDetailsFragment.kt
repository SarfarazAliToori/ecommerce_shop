package com.acclivousbyte.shopee.view.fragments.promotionDetail

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.color
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentPromotionDetailsBinding
import com.acclivousbyte.shopee.extensions.mutedColor
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.activties.base.MainActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionsViewModel
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PromotionDetailsFragment : MainMVVMNavigationFragment<PromotionsViewModel>(PromotionsViewModel::class) {

    private lateinit var binding: FragmentPromotionDetailsBinding
    private val promotionViewModel: PromotionsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val args by navArgs<PromotionDetailsFragmentArgs>()
    private var quantity = 1
    private lateinit var cartData1: CartData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPromotionDetailsBinding.inflate(inflater, container, false)


        setupPromotionDetail()

        return binding.root
    }

    private fun setupPromotionDetail() {
        binding.fragmentPromotionsDetailHeadingTv.run {
            text = args.promotionData.title
        }
        binding.fragmentPromotionsDetailDescriptionHeadingTv.run {
            text = args.promotionData.short_description
        }
        binding.fragmentPromotionsDetailDetailTv.run {
            text = args.promotionData.features
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(args.promotionData.device_image, ScaleTypes.FIT))

        val slider = binding.fragmentPromotionsDetailImageSlider
        slider.setImageList(imageList)

//        Glide.with(requireContext())
//            .load(args.promotionData.device_image).into(binding.fragmentPromotionsDetailImageIv)

        //For Strike Text.
        val price =  binding.fragmentPromotionsDetailOldPriceTv
        price.text = "RS, ${args.promotionData.old_price.toString()}"
        price.paintFlags = price.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG;


        val discountedPrice = args.promotionData.price.toString()
        binding.fragmentPromotionsDetailNewPriceTv.text = let {
            SpannableStringBuilder().color(requireContext().mutedColor(R.color.red)){append("RS,")}.append("$discountedPrice")
        }

        observe(promotionViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
               // val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentPromotionsPromotionDetailsBannerIv.promotionsBannerIv)

                binding.fragmentPromotionsPromotionDetailsBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(
                        adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        promotionViewModel.generalAds()
        setupProgressDialog(promotionViewModel.showHideProgressDialog, dialogHelper)

        binding.fragmentPromotionsDetailAddToCart.setOnClickListener {
            checkUserAndAddProductsToCart()
        }

    }


    private fun checkUserAndAddProductsToCart() {


        val id = args.promotionData.id.toString()

        val isProductId = MySingleton.listProductId.contains(id)

        if (isProductId) {
            showAlert("Alert !", "Already Available in Cart!\nYou can increase or decrease the quantity from cart.","Ok"){}

        } else {
            showAlert("Alert !", "You want to add product to Cart!", "Yes", "No"){
                MySingleton.listProductId.add(args.promotionData.id.toString())
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
               args.promotionData.id.toString(),
               args.promotionData.title,
               args.promotionData.short_description,
               args.promotionData.features,
               args.promotionData.price.toString(),
               args.promotionData.device_image,
               "promotion",
           )

        MySingleton.cartDataList.add(cartData)

    }


}