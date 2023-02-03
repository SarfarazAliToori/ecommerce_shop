package com.acclivousbyte.shopee.view.fragments.cart.cartCheckOut

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.placeOrder.PlaceOrderRequest
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import com.acclivousbyte.shopee.view.fragments.promotionDetail.PromotionDetailsFragmentDirections
import com.bumptech.glide.Glide
import com.stripe.android.PaymentConfiguration
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardInputWidget
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.acclivousbyte.shopee.databinding.FragmentCartCheckOutBinding
import com.acclivousbyte.shopee.utils.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.createCardToken
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.model.Token


class CartCheckOutFragment :
    MainMVVMNavigationFragment<CartCheckOutViewModel>(CartCheckOutViewModel::class) , ApiResultCallback<Token>{

    private lateinit var binding: FragmentCartCheckOutBinding
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val cartCheckOutViewModel: CartCheckOutViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()

    private lateinit var stripe: Stripe
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher
    private val publishableKey = "pk_test_51JIsjoJDJJJr4FA7cIvpqE0xI607R6eqiHlgHtMKj85WwJ6oP6oOiX1VxTfWXIZd0VBvTv2AFMghssNrM8WG1KiK00HfsuMG1B"
    var cardToken: String = ""
    var cardLast4digits: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCartCheckOutBinding.inflate(inflater, container, false)

        stripe = Stripe(requireContext(), publishableKey)

        setupCheckOutData()
        loadGeneralAds()

        val paymentConfiguration = PaymentConfiguration.getInstance(requireContext())
        val dd = PaymentConfiguration.init(requireContext(), publishableKey)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )


        binding.fragmentCartCheckOutBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    private fun loadGeneralAds() {
        observe(cartCheckOutViewModel.generalAdsMutableLiveData) {
            if (!it.consumed) it.consume()?.let {
                //val randomAdsImages = it.random().images.random()
                val randomAds = it.random()
                val adsLink = randomAds.url
                val randomImages = randomAds.images.random()
                Glide.with(requireContext())
                    .load(randomImages)
                    .skipMemoryCache(false)
                    .into(binding.fragmentCartCheckOutAdsId.promotionsBannerIv)

                binding.fragmentCartCheckOutAdsId.promotionsBannerIv.setOnClickListener {
                    val action = PromotionDetailsFragmentDirections.actionPromotionDetailsFragmentToWebViewFragment(adsLink)
                    findNavController().navigate(action)
                }
            }
        }

        cartCheckOutViewModel.generalAds()
        setupProgressDialog(cartCheckOutViewModel.showHideProgressDialog, dialogHelper)
    }

    private fun setupCheckOutData() {
        val userAddress = sharePreferenceHelper.getUserData()?.address
        binding.fragmentCartCheckOutAddress.setText(userAddress.toString())

        val deliveryCheck = binding.fragmentCartCheckOutCashOnDelivery
        val stripCheck = binding.fragmentCartCheckOutStrip

        deliveryCheck.setOnClickListener {
            if (deliveryCheck.isChecked) {
                stripCheck.isChecked = false
            }
        }

        stripCheck.setOnClickListener {
            if (stripCheck.isChecked) {
                deliveryCheck.isChecked = false
                showStripeDialog()
            }
        }


        binding.fragmentCartCheckOutPlaceOrder.setOnClickListener {
            val userId  = sharePreferenceHelper.getUserData()?.id
            val userAdd = sharePreferenceHelper.getUserData()?.address
            val userPhoneNum = sharePreferenceHelper.getUserData()?.phone_number
            val cartItems = MySingleton.cartDataList
//            val cartItemsList = Item(1, "2", "Promotion")
            val listofItems = MySingleton.listItems

            val placeOrder = PlaceOrderRequest(
                userId,
                listofItems,
                userAdd,
                userPhoneNum,
                cardToken
            )
            val authToken = sharePreferenceHelper.getUserData()?.token
            if (authToken != null) {
                cartCheckOutViewModel.createUserOrder(authToken, placeOrder)
            }

            observe(cartCheckOutViewModel.orderSummaryLiveData) {
                if (!it.consumed) it.consume()?.let {
                    if (it != null) {
                        val address = binding.fragmentCartCheckOutAddress.text.toString()
                        val action = CartCheckOutFragmentDirections.actionCartCheckOutFragmentToOrderSummaryFragment(it, address)
                        findNavController().navigate(action)
                    } else {
                        Log.i("post_order", "Post Order Error")
                    }
                }
            }

            setupProgressDialog(cartCheckOutViewModel.showHideProgressDialog, dialogHelper)

        }


        binding.fragmentCartCheckOutTotalPrice.run {
            text = AppUtils.GTotalPrice.toString()
        }

        binding.fragmentCartCheckOutSubtotal.run {
            text = AppUtils.GTotalPrice.toString()
        }

    }

    private fun showStripeDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_payment_dialog)
        val addCardButton = dialog.findViewById<Button>(R.id.addPaymentCardButton)
        val cardInputWidget = dialog.findViewById<CardInputWidget>(R.id.cardInputWidget)
        val back = dialog.findViewById<TextView>(R.id.back)
        cardInputWidget.postalCodeEnabled = false
        back.setOnClickListener {
            dialog.dismiss()
        }
        // Confirm the PaymentIntent with the card widget
        addCardButton.setOnClickListener {

            if (cardInputWidget.paymentMethodCreateParams != null) {
//                stripe.createCardToken(cardInputWidget.cardParams!!, publishableKey, callback = this)
                binding.fragmentOrderCheckOutCardInfo.run {
                    text = "**** **** ****" + cardLast4digits
                    setOnClickListener {
                        showStripeDialogWithParam(cardInputWidget.paymentMethodCreateParams!!)
                    }
                }
                val dd: String? = cardInputWidget.paymentMethodCreateParams?.card?.toString()

            } else {
                Toast.makeText(activity, "Add is not added", Toast.LENGTH_SHORT).show()
            }

            dialog.dismiss()
            stripe.createCardToken(cardInputWidget.cardParams!!, publishableKey, callback = this)
        }
        dialog.show()

    }

    private fun showStripeDialogWithParam(paymentMethodCreateParams: PaymentMethodCreateParams) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.card_payment_dialog)
        val addCardButton = dialog.findViewById<Button>(R.id.addPaymentCardButton)
        val cardInputWidget = dialog.findViewById<CardInputWidget>(R.id.cardInputWidget)
        val back = dialog.findViewById<TextView>(R.id.back)
        back.setOnClickListener {
            dialog.dismiss()
        }

        val cardInfo = sharePreferenceHelper.getPaymentCardData()

        val ss = cardInfo

    }


    private fun onPaymentResult(paymentResult: PaymentResult) {
        val message = when (paymentResult) {
            is PaymentResult.Completed -> {
                "Completed!"
            }
            is PaymentResult.Canceled -> {
                "Canceled!"
            }
            is PaymentResult.Failed -> {
                // This string comes from the PaymentIntent's error message.
                // See here: https://stripe.com/docs/api/payment_intents/object#payment_intent_object-last_payment_error-message
                "Failed: " + paymentResult.throwable.message
            }
        }

    }

    override fun onError(e: Exception)  {
        Log.e("tokenException - > ", e.message.toString())
    }

    override fun onSuccess(result: Token) {
        Log.e("stripeToken = ? " , result.toString())
        cardToken = result.toString()
        cardLast4digits = result.card?.last4.toString()
    }

}

