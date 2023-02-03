package com.acclivousbyte.shopee.view.fragments.brands

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentBrandsBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class BrandsFragment : MainMVVMNavigationFragment<BrandsViewModel>(BrandsViewModel::class) {

    private lateinit var brandsAdapter: BrandsAdapter
    private lateinit var binding: FragmentBrandsBinding
    private val brandsViewModel: BrandsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrandsBinding.inflate(inflater, container, false)

        loadBrandData()

        return binding.root
    }

    private fun loadBrandData() {

        val recyclerView = binding.fragmentBrandsRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        brandsAdapter = BrandsAdapter(requireContext()) {
            val action = BrandsFragmentDirections.actionBrandsFragmentToBrandDetailFragment1(it)
            findNavController().navigate(action)
        }
        recyclerView.adapter = brandsAdapter

        observe(brandsViewModel.brandsListMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                brandsAdapter.submitList(it.brands)
            }
        }

        observe(brandsViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
//                val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()

                Glide.with(binding.fragmentBrandsPromotionBannerIv.promotionsBannerIv)
                    .load(randomImages)
                    .into(binding.fragmentBrandsPromotionBannerIv.promotionsBannerIv)

                binding.fragmentBrandsPromotionBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = BrandsFragmentDirections.actionBrandsFragmentToWebViewFragment(
                        adsLink
                    )
                    findNavController().navigate(action)
                }
            }
        }

        setupProgressDialog(brandsViewModel.showHideProgressDialog, dialogHelper)
        brandsViewModel.generalAds()
        brandsViewModel.brandsList()
    }


}