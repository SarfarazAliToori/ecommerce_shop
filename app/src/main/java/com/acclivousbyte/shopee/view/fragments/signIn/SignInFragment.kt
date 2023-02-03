package com.acclivousbyte.shopee.view.fragments.signIn

import android.app.Activity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentSignInBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.user.UserLoginRequestModel
import com.acclivousbyte.shopee.utils.AppUtils
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.utils.SharePreferenceHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : MainMVVMNavigationFragment<SignInViewModel>(SignInViewModel::class) {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModel()
    private val sharePreferenceHelper: SharePreferenceHelper by inject()
    private val dialogHelper: MaterialDialogHelper by inject()
    private var mIsShowPassword: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

//        if (sharePreferenceHelper.getIsUserLoggedIn()) {
//            Intent(requireContext(), HomeActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(it)
//            }
//        }

        binding.fragmentSignInBackTv.setOnClickListener {
            findNavController().popBackStack()
        }

        if (sharePreferenceHelper.getUserData() != null) {
//            Intent(requireContext(), HomeActivity::class.java).also {
//                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(it)
//            }
            findNavController().navigate(R.id.adsHomeFragment)
        }

        binding.fragmentSignInSignupTv.setOnClickListener {
            //findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment2)
        }

        binding.fragmentSignInForgotYourPassTv.setOnClickListener {
            //findNavController().navigate(R.id.action_signInFragment_to_forgotPasswordFragment)
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment2)
        }

        binding.fragmentSignInLoginBtn.setOnClickListener {
            userLogIn()
        }

        binding.fragmentSignInShowHideIv.setOnClickListener {
            mIsShowPassword = !mIsShowPassword  // mIsShowPassword = true
            showHidePassword(mIsShowPassword)
        }
        showHidePassword(mIsShowPassword)

        return binding.root
    }

    private fun showHidePassword(isShow: Boolean) {
        if (isShow) {
            binding.fragmentSignInPasswordEd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.fragmentSignInShowHideIv.setImageResource(R.drawable.show_pass_icon)
        } else {
            binding.fragmentSignInPasswordEd.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.fragmentSignInShowHideIv.setImageResource(R.drawable.hide_pass_icon)
        }
        binding.fragmentSignInPasswordEd.run {
            setSelection(this.text.toString().length)
        }
    }

    private fun userLogIn() {
        val email = binding.fragmentSignInEmailEd.text.toString()
        val password = binding.fragmentSignInPasswordEd.text.toString()

        when {
            isValidEmail(email).not() -> {
                binding.fragmentSignInEmailEd.error = "Please enter valid email"
            }
            password.isEmpty() -> {
                binding.fragmentSignInPasswordEd.error = "Please enter valid password"
            }
            else -> {
                lifecycleScope.launchWhenStarted {
                    val userLoginRequestModel = UserLoginRequestModel(
                        email, password
                    )
                    signInViewModel.userLogIn(userLoginRequestModel)
                }
                observe(signInViewModel.userLogInLiveData) {
                    if (!it.consumed) it.consume()?.let {
                        if (it.token != null) {
//                            Intent(requireContext(), HomeActivity::class.java).also {
//                                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                startActivity(it)
//                            }
                            showAlert("Alert!", "LogIn Successfully.", "OK", null) {
                                val homeActivity = requireActivity() as HomeActivity
                                if (AppUtils.isFromCartFragment || AppUtils.isFromProductDetail) {
                                    findNavController().popBackStack()
                                    homeActivity.recreate()
                                } else {
                                    findNavController().navigate(R.id.adsHomeFragment)
                                    homeActivity.recreate()
                                }
                            }

                        }
                    } else {
                        showAlert("Alert!", "Username and password is incorrect please check!", "Ok", null ){}
                    }
                }
                setupProgressDialog(signInViewModel.showHideProgressDialog, dialogHelper)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
