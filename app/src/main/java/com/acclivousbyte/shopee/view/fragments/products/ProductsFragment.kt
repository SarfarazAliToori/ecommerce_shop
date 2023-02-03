package com.acclivousbyte.shopee.view.fragments.products

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.databinding.FragmentProductsBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.productsList.ProductsListData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.activties.base.MainActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductsFragment : MainMVVMNavigationFragment<ProductsViewModel>(ProductsViewModel::class), OnClickListener {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var productsAdapter: ProductsAdapter
    private val productsViewModel: ProductsViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private lateinit var url: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)

        loadProductListingData()

        return binding.root
    }

    private fun loadProductListingData() {

        val recyclerView = binding.fragmentProductsRecyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productsAdapter = ProductsAdapter(requireContext(),this){
            val action = ProductsFragmentDirections.actionProductsFragmentToProductsDetailFragment(it)
            findNavController().navigate(action)
        }
        recyclerView.adapter = productsAdapter

        observe(productsViewModel.generalAdsMutableLiveData){
            if (!it.consumed) it.consume()?.let {

//                // ads slider.
//                val imageList = ArrayList<SlideModel>()
//                it.forEach {
////                    it.images.forEach {
////                       imageList.add(SlideModel(it))
////                   }
//                    imageList.add(SlideModel(it.images.get(0)))
//                    url = it.url
//                }
//                val slider = binding.adsSlider
//                slider.setImageList(imageList)
//                slider.setItemClickListener(object : ItemClickListener {
//                    override fun onItemSelected(position: Int) {
//                        Log.i("ff", "position $position")
//
//                        val action = ProductsFragmentDirections.actionProductsFragmentToWebViewFragment(url)
//                        findNavController().navigate(action)
//                    }
//                })


                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentProductsPromotionBannerIv.promotionsBannerIv)

                binding.fragmentProductsPromotionBannerIv.promotionsBannerIv.setOnClickListener {
                    val action = ProductsDetailFragmentDirections.actionProductsDetailFragmentToWebViewFragment(
                        adsLink)
                    findNavController().navigate(action)
                }

            }
        }


        if (AppUtils.isFromFilter) {
            /**
             * Query params for Filters in Get Products API:
            query, category_id, manufacturer_id, brand_id, model_id, year_id, page, per_page
             */

            val queryOptions: HashMap<String, Int> = HashMap()
            queryOptions["category_id"] = AppUtils.category_id
            queryOptions["manufacturer_id"] = AppUtils.manufacturer_id
            queryOptions["brand_id"] = AppUtils.brand_id
            queryOptions["model_id"] = AppUtils.model_id
            queryOptions["year_id"] = AppUtils.year_id
//            queryOptions["page"] = 1
//            queryOptions["per_page"] = 1

            productsViewModel.filterProductsListUsingQueryMap(queryOptions)

            observe(productsViewModel.filterProductListMutableLiveData) {
                if (!it.consumed) it.consume()?.let {
                    productsAdapter.submitList(it)
                }
            }

        } else {
            observe(productsViewModel.productListMutableLiveData) {
                if (!it.consumed) it.consume()?.let {
                val list = it.sortedByDescending {
                            it.id
                    }
                    productsAdapter.submitList(list)
                }
            }
        }

        setupProgressDialog(productsViewModel.showHideProgressDialog, dialogHelper)
        productsViewModel.generalAds()
        productsViewModel.productsList()

    }

    // add to cart click
    override fun onAddToCartClick(productsListData: ProductsListData) {
        checkUserAndAddProductsToCart(productsListData)
    }

    private fun checkUserAndAddProductsToCart(productsListData: ProductsListData) {

//        if (sharePreferenceHelper.getUserData() != null) {
//
//
//        } else {
//            showAlert("Alert !", "Please login first!","Yes", "No"){
//                val intent = Intent(requireContext(), MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//            }
//        }

        val id = productsListData.id.toString()

        val isProductId = MySingleton.listProductId.contains(id)

        if (isProductId) {
            showAlert("Alert !", "Already Available in Cart.\nYou can increase or decrease the quantity from cart.","Ok"){}

        } else {
            showAlert("Alert !", "You want to add product to Cart.", "Yes", "No"){
                MySingleton.listProductId.add(productsListData.id.toString())
                addToCartProducts(productsListData)
                // we want to increment cart count, so that's why I want to recreate the activity.
                val homeActivity = requireActivity() as HomeActivity
                homeActivity.recreate()
            }
        }

    }

    private fun addToCartProducts(productsListData: ProductsListData) {
        AppUtils.showHideCartBadge = true
        AppUtils.badgeCounter++

        val cartData = CartData(
            productsListData.id.toString(),
            productsListData.title,
            productsListData.description,
            productsListData.why,
            productsListData.rate.toString(),
            productsListData.logo,
            "product",
        )

        MySingleton.cartDataList.add(cartData)
    }

}