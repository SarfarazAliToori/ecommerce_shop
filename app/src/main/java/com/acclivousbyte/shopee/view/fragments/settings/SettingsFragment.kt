package com.acclivousbyte.shopee.view.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.databinding.FragmentSettingsBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionsListFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : MainMVVMNavigationFragment<SettingsViewModel>(SettingsViewModel::class) {

    private lateinit var binding: FragmentSettingsBinding
    private val settingViewModel: SettingsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.fragmentSettingBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fragmentSettingSave.setOnClickListener {
            Toast.makeText(activity, "not implemented yet.", Toast.LENGTH_SHORT).show()
        }

        loadSettingGeneralAds()

        return binding.root
    }

    private fun loadSettingGeneralAds() {
        observe(settingViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentSettingPromotionBannerIv.promotionsBannerIv)

                binding.fragmentSettingPromotionBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = PromotionsListFragmentDirections.actionPromotionsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        settingViewModel.generalAds()
        setupProgressDialog(settingViewModel.showHideProgressDialog, dialogHelper)
    }

}