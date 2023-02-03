package com.acclivousbyte.shopee.view.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentProfileBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.updateProfile.UpdateUserProfileRequest
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : MainMVVMNavigationFragment<ProfileViewModel>(ProfileViewModel::class) {


    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel by viewModel<ProfileViewModel>()
    private val sharePreferenceHelper by inject<SharePreferenceHelper>()
    private val dialogHelper by inject<MaterialDialogHelper>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        loadGeneralAds()
        setDataFromSharePre()

        binding.fragmentProfileBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fragmentProfileSave.setOnClickListener {
            saveProfileData()
        }

        return binding.root
    }

    private fun loadGeneralAds() {
        observe(profileViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                // val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentProfileAdsBanner.promotionsBannerIv)

                binding.fragmentProfileAdsBanner.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(
                        adsLink)
                    findNavController().navigate(action)
                }
            }
        }
        profileViewModel.generalAds()
    }

    private fun setDataFromSharePre() {

        val userData = sharePreferenceHelper.getUserData()
        binding.fragmentProfileFirstName.setText(userData?.first_name)
        binding.fragmentProfileSecondName.setText(userData?.last_name)
        binding.fragmentProfileEmail.setText(userData?.email)
        binding.fragmentProfilePhoneNum.setText(userData?.phone_number)
        binding.fragmentProfileAddress.setText(userData?.address)
    }


    private fun saveProfileData() {
        val firstName = binding.fragmentProfileFirstName.text.toString()
        val secondName = binding.fragmentProfileSecondName.text.toString()
        val email = binding.fragmentProfileEmail.text.toString()
        val phoneNum = binding.fragmentProfilePhoneNum.text.toString()
        val address = binding.fragmentProfileAddress.text.toString()

        when {
            firstName.isEmpty() -> {
                binding.fragmentProfileFirstName.error = "Please enter first name"
            }
            secondName.isEmpty() -> {
                binding.fragmentProfileSecondName.error = "Please enter second name"
            }
            isValidEmail(email).not() -> {
                binding.fragmentProfileEmail.error = "Please enter valid email address"
            }
            isValidPhoneNum(phoneNum).not() -> {
                binding.fragmentProfilePhoneNum.error = "Please enter phone number"
            }
            address.isEmpty() -> {
                binding.fragmentProfileAddress.error = "Please enter address"
            }
            else -> {

                val userId = sharePreferenceHelper.getUserData()?.id
                val userToken = sharePreferenceHelper.getUserData()?.token
                if (userId != null && userToken != null) {
                    val requestData = UpdateUserProfileRequest(
                        address, email, firstName, secondName, phoneNum,userId
                    )
                    profileViewModel.updateUserProfile(userToken, requestData)

                    observe(profileViewModel.userUpdateResponseLiveData) {
                        if (!it.consumed) it.consume()?.let {
                            if (it != null) {
                                showAlert("Alert!", "Profile Updated Successfully.", "OK", null) {
                                    findNavController().navigate(R.id.adsHomeFragment)
                                }
                            }
                        }
                    }

                }
                setupProgressDialog(profileViewModel.showHideProgressDialog, dialogHelper)

            }
        }
    }

}