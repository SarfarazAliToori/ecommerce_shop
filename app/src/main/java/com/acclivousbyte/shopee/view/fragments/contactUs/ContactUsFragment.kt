package com.acclivousbyte.shopee.view.fragments.contactUs

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.databinding.FragmentContactUsBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionsListFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ContactUsFragment : MainMVVMNavigationFragment<ContactUsViewModel>(ContactUsViewModel::class) {

    private lateinit var binding: FragmentContactUsBinding
    private val contactUsViewModel: ContactUsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactUsBinding.inflate(inflater, container, false)

        binding.fragmentContactUsPhoneNumberTv.run {
            setOnClickListener {
                val contactNum = this.text.toString()
                openPhoneDialer(contactNum)
            }
        }

        binding.fragmentContactUsMobileNumberTv.run {
            setOnClickListener {
                val mobileNum = this.text.toString()
                openPhoneDialer(mobileNum)
            }
        }

        binding.fragmentContactUsEmailTv.run {
            setOnClickListener {
                val email = this.text.toString()
                openEmailApp(email)
            }
        }

        loadGeneralAds()

        return binding.root
    }

    private fun openEmailApp(email: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto: $email"))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun openPhoneDialer(contactNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$contactNum")
        startActivity(intent)
    }

    private fun loadGeneralAds() {

        observe(contactUsViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentContactUsPromotionBannerIv.promotionsBannerIv)

                binding.fragmentContactUsPromotionBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = PromotionsListFragmentDirections.actionPromotionsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        contactUsViewModel.generalAds()
        setupProgressDialog(contactUsViewModel.showHideProgressDialog, dialogHelper)

    }


}