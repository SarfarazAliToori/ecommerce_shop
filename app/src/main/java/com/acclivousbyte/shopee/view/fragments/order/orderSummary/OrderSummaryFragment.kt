package com.acclivousbyte.shopee.view.fragments.order.orderSummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentOrderSummaryBinding
import com.acclivousbyte.shopee.extensions.gone
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderSummaryFragment() : MainMVVMNavigationFragment<OrderSummaryViewModel>(OrderSummaryViewModel::class) {

    private lateinit var binding: FragmentOrderSummaryBinding
    private lateinit var orderSummaryAdapter: OrderSummaryAdapter
    private val orderSummaryViewModel: OrderSummaryViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val args: OrderSummaryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderSummaryBinding.inflate(inflater, container, false)



        loadGeneralAds()
        orderSummary()

        binding.fragmentOrderSummaryBackTv.setOnClickListener {
            MySingleton.cartDataList.clear()
            MySingleton.listProductId.clear()
            AppUtils.badgeCounter = 0
            findNavController().navigate(R.id.adsHomeFragment)
//            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun loadGeneralAds() {
        observe(orderSummaryViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentOrderSummaryAdsId.promotionsBannerIv)

                binding.fragmentOrderSummaryAdsId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        orderSummaryViewModel.generalAds()
        setupProgressDialog(orderSummaryViewModel.showHideProgressDialog, dialogHelper)

    }

    private fun orderSummary() {
        orderSummaryAdapter = OrderSummaryAdapter {  }
        val recyclerView = binding.fragmentOrderSummaryRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderSummaryAdapter

           orderSummaryAdapter.submitList(MySingleton.cartDataList)

        binding.fragmentOrderSummarOrderId.text = args.orderSummaryData.id.toString()

        binding.fragmentOrderSummarAddress.text = args.addressString

        binding.fragmentOrderSummarTotalPrice.text = args.orderSummaryData.total_price.toString()

//        binding.fragmentOrderSummaryNoteLayout.run {
//             if (args.noteString.isEmpty()) {
//                 this.gone()
//             } else {
//                 binding.fragmentOrderSummarNoteTv.text = args.noteString
//             }
//        }

    }

}