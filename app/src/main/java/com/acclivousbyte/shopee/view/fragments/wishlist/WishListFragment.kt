package com.acclivousbyte.shopee.view.fragments.wishlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentWishListBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.extensions.visible
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.models.productsList.ProductsListData
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.MySingleton
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionsListFragmentDirections
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WishListFragment : MainMVVMNavigationFragment<WishListViewModel>(WishListViewModel::class), WishListListener {


    private lateinit var binding: FragmentWishListBinding
    private lateinit var wishlistAdapter: WishListAdapter
    private val wishListViewModel: WishListViewModel by viewModel()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val dialogHelper: MaterialDialogHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWishListBinding.inflate(inflater, container, false)

        loadWishlistData()
        loadGeneralAds()

        return binding.root
    }

    private fun loadWishlistData() {
        wishlistAdapter = WishListAdapter(this) {
            Log.i("wishlist", "Wishlist item clicked")
            val action = WishListFragmentDirections.actionWishListFragmentToProductsDetailFragment(it)
            findNavController().navigate(action)
        }
        val recyclerView = binding.fragmentWishListRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = wishlistAdapter

        val wishListData = sharePreferenceHelper.getWishlistProducts()

        if (wishListData.isEmpty()) {
            binding.fragmentWishListMessageTv.run {
                visible()
                text = getString(R.string.your_wishlist_is_empty)
            }

//            showAlert("Alert!", getString(R.string.your_wishlist_is_empty),
//            "Ok", "", ){
//                findNavController().popBackStack()
//            }
        } else {
            wishlistAdapter.submitList(wishListData)
        }

        // gesture swipe delete
        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        //wishlistAdapter.deleteItem(viewHolder.absoluteAdapterPosition)
                        val list = wishListData.removeAt(viewHolder.absoluteAdapterPosition)
                        val updatedList: MutableList<ProductsListData> = mutableListOf()

                        updatedList.add(list)
                        sharePreferenceHelper.setWishlistProducts(updatedList)
                        wishlistAdapter.submitList(wishListData)
                        wishlistAdapter.notifyDataSetChanged()
                    }
                    else -> {
                        Log.i("dd", "")
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.fragmentWishListRecyclerView)

    }


    private fun loadGeneralAds() {
        observe(wishListViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                val randomAdsImages = it.random().images.random()
                Glide.with(requireContext())
                    .load(randomAdsImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentWishListAdsId.promotionsBannerIv)

                binding.fragmentWishListAdsId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionsListFragmentDirections.actionPromotionsFragmentToWebViewFragment(randomAdsImages)
                    findNavController().navigate(action)
                }
            }
        }

        wishListViewModel.generalAds()
        setupProgressDialog(wishListViewModel.showHideProgressDialog, dialogHelper)
    }

    override fun onClickAddToCart(productsListData: ProductsListData) {
        checkUserAndAddProductsToCart(productsListData)
    }

    private fun checkUserAndAddProductsToCart(productsListData: ProductsListData) {


        val id = productsListData.id.toString()

        val isProductId = MySingleton.listProductId.contains(id)

        if (isProductId) {
            showAlert("Alert !", "Already Available in Cart!\nYou can increase or decrease the quantity from cart.","Ok"){}

        } else {
            showAlert("Alert !", "You want to add product to Cart!", "Yes", "No"){
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