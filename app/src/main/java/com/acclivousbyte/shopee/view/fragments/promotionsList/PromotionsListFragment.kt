package com.acclivousbyte.shopee.view.fragments.promotionsList

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentPromotionsBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PromotionsListFragment :
    MainMVVMNavigationFragment<PromotionsViewModel>(PromotionsViewModel::class) {

    private var _binding: FragmentPromotionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PromotionListAdapter
    private val promotionsListViewModel: PromotionsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPromotionsBinding.inflate(inflater, container, false)

        loadPromotionData()

        return binding.root
    }

    private fun loadPromotionData() {

        adapter = PromotionListAdapter(requireContext()) {
            val action = PromotionsListFragmentDirections.actionPromotionsFragmentToPromotionDetailsFragment(it)
            findNavController().navigate(action)
           // findNavController().navigate(R.id.promotionDetailsFragment)
        }

        val recyclerView = binding.fragmentPromotionsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observe(promotionsListViewModel.promotionMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                val listData = it.data.sortedByDescending {
                    it.id
                }
                adapter.submitList(listData)
            }
        }

        observe(promotionsListViewModel.generalAdsMutableLiveData){
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentPromotionsPromotionBannerIv.promotionsBannerIv)

                binding.fragmentPromotionsPromotionBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = PromotionsListFragmentDirections.actionPromotionsFragmentToWebViewFragment(
                        adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        setupProgressDialog(promotionsListViewModel.showHideProgressDialog, dialogHelper)
        promotionsListViewModel.generalAds()
        promotionsListViewModel.promotionListItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}