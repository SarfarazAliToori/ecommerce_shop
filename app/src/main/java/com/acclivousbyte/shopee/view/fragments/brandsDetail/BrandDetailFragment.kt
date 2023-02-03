package com.acclivousbyte.shopee.view.fragments.brandsDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.acclivousbyte.shopee.databinding.FragmentBrandDetailBinding
import com.acclivousbyte.shopee.extensions.gone
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.brands.BrandsViewModel
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrandDetailFragment : MainMVVMNavigationFragment<BrandsViewModel>(BrandsViewModel::class) {


    private lateinit var binding: FragmentBrandDetailBinding
    private val brandsViewModel: BrandsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val args by navArgs<BrandDetailFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrandDetailBinding.inflate(inflater, container, false)
        //Showing the title, but problem is that when I go to detail first
        // time it will not change the title, when I go back and click again
        // on items it will update the title.
//        findNavController().currentDestination!!.label = "Hello"

        setupBrandDetail()
        return binding.root
    }

    private fun setupBrandDetail() {
        findNavController().currentDestination!!.label = args.brandsDetails.name

        binding.fragmentAdsHomeBrandDetailTitle.run {
            text = args.brandsDetails.name
        }

        binding.fragmentAdsHomeBrandDetailDescription.run {
            text = args.brandsDetails.description
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(args.brandsDetails.logo_url))
        val slider = binding.fragmentBrandDetail1ImageSlider
        slider.setImageList(imageList)

        binding.fragmentAdsHomeBrandDetailWebIcon.setOnClickListener {
            if (args.brandsDetails.web_url != null) {
                val action = BrandDetailFragmentDirections.actionBrandDetailFragmentToWebViewFragment(
                    args.brandsDetails.web_url!!
                )
                findNavController().navigate(action)
            } else {
                it.gone()
            }
        }

        binding.fragmentAdsHomeBrandDetailYoutubeIcon.setOnClickListener {
            if (args.brandsDetails.youtube_url != null) {
                val action = BrandDetailFragmentDirections.actionBrandDetailFragmentToWebViewFragment(
                    args.brandsDetails.youtube_url!!
                )
                findNavController().navigate(action)
            }
        }

        observe(brandsViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentAdsHomeBrandDetailBannerId.promotionsBannerIv)

                binding.fragmentAdsHomeBrandDetailBannerId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(
                        adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        brandsViewModel.generalAds()
        setupProgressDialog(brandsViewModel.showHideProgressDialog, dialogHelper)
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentBrandDetailBinding.bind(view)
//    }

}