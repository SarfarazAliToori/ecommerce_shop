package com.acclivousbyte.shopee.view.fragments.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentCartBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.extensions.visible
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.placeOrder.Item
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : MainMVVMNavigationFragment<CartViewModel>(CartViewModel::class), CartAdapter.OnClickListener {


    private lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val cartViewModel: CartViewModel by viewModel()
    private var totalPrice: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCartBinding.inflate(inflater, container, false)

        loadCartProducts()
        loadGeneralAds()

        binding.fragmentCartCheckOutBtn.setOnClickListener {

            if (sharePreferenceHelper.getUserData() != null) {
                findNavController().navigate(R.id.action_cartFragment_to_cartCheckOutFragment)
            } else {
                showAlert("Alert !", "Please login first!","Yes", "No"){
                    AppUtils.isFromCartFragment = true
                    findNavController().navigate(R.id.action_cartFragment_to_signInFragment)
                }
            }

        }

        binding.fragmentCartBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun loadCartProducts() {
        val recyclerView = binding.fragmentCartRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cartAdapter = CartAdapter(requireContext(), this)

        recyclerView.adapter = cartAdapter


        if (MySingleton.cartDataList.isEmpty()) {
            binding.fragmentCartEmptyCartMessageTv.run {
                visible()
                text = getString(R.string.your_cart_is_empty)
            }
            binding.fragmentCartCheckOutBtn.isEnabled = false
        } else {
            cartAdapter.submitList(MySingleton.cartDataList)
        }

        // for user create order api.
        MySingleton.cartDataList.forEach {
            val items = Item(
                it.productsId,
                it.productQuantity.toString(),
                it.productType
            )
            MySingleton.listItems.add(items)
            val dd = MySingleton.listItems
        }

        refreshPaymentDetail()

    }

    private fun refreshPaymentDetail() {
        binding.fragmentCartTotalPrice.run {
            totalPrice = calculateTotalPrice(MySingleton.cartDataList)
            text = totalPrice.toString()
            AppUtils.GTotalPrice = totalPrice
        }

        binding.fragmentCartSubTotal.run {
            text = totalPrice.toString()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onPlus(position: Int) {

        val dd = MySingleton.cartDataList.filter {
            it.productQuantity == it.productQuantity
        }.get(position).productQuantity

        if (dd >= 1) {
          MySingleton.cartDataList.filter {
                it.productQuantity == it.productQuantity
            }.get(position).productQuantity++
        }

        cartAdapter.notifyDataSetChanged()
        refreshPaymentDetail()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onMinus(position: Int) {

//        showAlert("Alert!","Are you sure, You Want to increase the the product quantity?",
//            "Yes","No"
//        ) {}

        val dd = MySingleton.cartDataList.filter {
            it.productQuantity == it.productQuantity
        }.get(position).productQuantity

        if (dd > 1) {
            MySingleton.cartDataList.filter {
                it.productQuantity == it.productQuantity
            }.get(position).productQuantity--
        }

        cartAdapter.notifyDataSetChanged()
        refreshPaymentDetail()

    }


    private fun calculateTotalPrice(list: MutableList<CartData>): Int {
        var tPrice = 0
        list.forEach {
            val perProduct = it.productsPrice?.toInt()?.let { it1 -> it.productQuantity.times(it1) }
            tPrice += perProduct!!
        }
        return tPrice
    }

    private fun loadGeneralAds() {

        observe(cartViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentCartBannerAdsId.promotionsBannerIv)

                binding.fragmentCartBannerAdsId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        cartViewModel.generalAds()
        setupProgressDialog(cartViewModel.showHideProgressDialog, dialogHelper)

    }


}