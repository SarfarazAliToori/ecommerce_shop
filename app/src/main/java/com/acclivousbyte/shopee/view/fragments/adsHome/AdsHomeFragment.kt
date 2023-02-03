package com.acclivousbyte.shopee.view.fragments.adsHome

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentAdsHomeBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class AdsHomeFragment : MainMVVMNavigationFragment<AdsHomeViewModel>(AdsHomeViewModel::class){

    private lateinit var adsHomeAdapter: AdsHomeAdapter
    private lateinit var binding: FragmentAdsHomeBinding
    private val adsHomeViewModel: AdsHomeViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdsHomeBinding.inflate(inflater, container, false)

        loadAdsData()

        return binding.root
    }

    private fun loadAdsData() {

        adsHomeAdapter = AdsHomeAdapter(requireContext()) {
            Log.i("home", "item is clicked,,,")
            val action = AdsHomeFragmentDirections.actionAdsHomeFragmentToWebViewFragment(it.url)
            findNavController().navigate(action)
        }

        val recyclerview = binding.fragmentAdsHomeRecycler
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = adsHomeAdapter

        observe(adsHomeViewModel.premiumAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let { data ->
                adsHomeAdapter.submitList(data)
            }
        }

        setupProgressDialog(adsHomeViewModel.showHideProgressDialog, dialogHelper)
        adsHomeViewModel.premiumAds()
    }

}