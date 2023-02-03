package com.acclivousbyte.shopee.view.fragments.order.orderListing

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentOrderListingBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.extensions.visible
import com.acclivousbyte.shopee.models.userOrders.UserOrderRequest
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.cart.cartCheckOut.CartCheckOutViewModel
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderListingFragment : MainMVVMNavigationFragment<OrderListingViewModel>(OrderListingViewModel::class) {

    private lateinit var binding: FragmentOrderListingBinding
    private lateinit var adapter: OrderListingAdapter
    private val orderListingViewModel: OrderListingViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderListingBinding.inflate(inflater, container, false)

        loadMyOrders()

        return binding.root
    }


    private fun loadMyOrders() {

        val recyclerView = binding.fragmentOrderListingRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = OrderListingAdapter {
            Log.i("orderList", "Order list items is clicked.")
            //findNavController().navigate(R.id.action_orderListingFragment_to_orderDetailsFragment)
        }
        recyclerView.adapter = adapter

        val userToken = sharePreferenceHelper.getUserData()?.token.toString()
        val userId = sharePreferenceHelper.getUserData()?.id?.let { UserOrderRequest(it) }
        if (userId != null) {
            orderListingViewModel.userOrders(userToken, userId)
        }

        observe(orderListingViewModel.userOrderMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                if (it.isEmpty()) {
                    binding.fragmentOrderListMessageTv.visible()
                } else {
                    val myOrders = it.sortedByDescending {
                        it.id
                    }
                    adapter.submitList(myOrders)
                }
            }
        }


        observe(orderListingViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentOrderListingBannerId.promotionsBannerIv)

                binding.fragmentOrderListingBannerId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        orderListingViewModel.generalAds()
        setupProgressDialog(orderListingViewModel.showHideProgressDialog, dialogHelper)
    }

}