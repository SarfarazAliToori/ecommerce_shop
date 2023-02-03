package com.acclivousbyte.shopee.view.fragments.signUp

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.FragmentSignUpBinding
import com.acclivousbyte.shopee.extensions.setupProgressDialog
import com.acclivousbyte.shopee.models.user.UserRegisterRequestModel
import com.acclivousbyte.shopee.utils.MaterialDialogHelper
import com.acclivousbyte.shopee.view.activties.HomeActivity
import com.acclivousbyte.shopee.view.fragments.base.MainMVVMNavigationFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Matcher
import java.util.regex.Pattern


class SignUpFragment : MainMVVMNavigationFragment<SignUpViewModel>(SignUpViewModel::class) {


    private lateinit var binding: FragmentSignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModel()
    private val dialogHelper: MaterialDialogHelper by inject()
    private lateinit var heardAboutUsFrom: Any
    private var isShowPassword: Boolean = false
    private var isShowConfirmPassword: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        backButton()
        loadSpinnerData()
        binding.fragmentSignUpSingUpBtn.setOnClickListener {
            userRegistration()
        }

        binding.fragmentSignUpShowHideIv.setOnClickListener {
            isShowPassword = !isShowPassword
            showHidePassword(isShowPassword)
        }

        binding.fragmentSignUpConfirmPassShowHideIv.setOnClickListener {
            isShowConfirmPassword = !isShowConfirmPassword
            showHideConfirmPassword(isShowConfirmPassword)
        }

        showHidePassword(isShowPassword)
        showHideConfirmPassword(isShowPassword)

        return binding.root
    }


    private fun showHidePassword(isShow: Boolean) {
        if (isShow) {
            binding.fragmentSignUpPasswordEd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.fragmentSignUpShowHideIv.setImageResource(R.drawable.show_pass_icon)
        } else {
            binding.fragmentSignUpPasswordEd.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.fragmentSignUpShowHideIv.setImageResource(R.drawable.hide_pass_icon)
        }
        binding.fragmentSignUpPasswordEd.run {
            setSelection(this.text.toString().length)
        }
    }
    private fun showHideConfirmPassword(isShow: Boolean) {
        if (isShow) {
            binding.fragmentSignUpConfirmPasswordEd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.fragmentSignUpConfirmPassShowHideIv.setImageResource(R.drawable.show_pass_icon)
        } else {
            binding.fragmentSignUpConfirmPasswordEd.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.fragmentSignUpConfirmPassShowHideIv.setImageResource(R.drawable.hide_pass_icon)
        }
        binding.fragmentSignUpConfirmPasswordEd.run {
            setSelection(this.text.toString().length)
        }
    }

    private fun userRegistration() {

        val firstName = binding.fragmentSignUpNameEd.text.toString()
        val lastName = binding.fragmentSignUpLastNameEd.text.toString()
        val email = binding.fragmentSignUpEmailEd.text.toString()
        val phone = binding.fragmentSignUpPhoneEd.text.toString()
        val address = binding.fragmentSignUpAddressEd.text.toString()
        val password = binding.fragmentSignUpPasswordEd.text.toString()
        val confirmPassword = binding.fragmentSignUpConfirmPasswordEd.text.toString()
        val referenceName = binding.fragmentSignUpReferenceEd.text.toString()
        val isVender = binding.fragmentSignUpIsVendorCheckBox.isChecked.toString()


        when {
            firstName.isEmpty() -> {
                binding.fragmentSignUpNameEd.error = "Please enter valid name"
            }
            lastName.isEmpty() -> {
                binding.fragmentSignUpLastNameEd.error = "Please enter valid last name"
            }
            isValidEmail(email).not() -> {
                binding.fragmentSignUpEmailEd.error = "Please enter valid email"
            }
            isValidPhoneNum(phone).not() ->  {
                binding.fragmentSignUpPhoneEd.error = "Please enter valid phone number"
            }
//            address.isEmpty() -> {
//                binding.fragmentSignUpAddressEd.error = "Please enter valid address"
//            }
            isValidPassword(password).not() -> {
                binding.fragmentSignUpPasswordEd.error = "Password must contain 8 characters and one alphanumeric."
            }
            isValidPassword(confirmPassword).not() -> {
                binding.fragmentSignUpConfirmPasswordEd.error = "Password must contain 8 characters and one alphanumeric"
            }
//            referenceName.isEmpty() -> {
//                binding.fragmentSignUpReferenceEd.error = "Please enter valid reference name"
//            }
            else -> {

                val isPasswordMatch = isPasswordMatching(password, confirmPassword)
                if (isPasswordMatch)  {

                    val userRegisterModel = UserRegisterRequestModel(
                        address, email, firstName, null, isVender,
                        lastName, password, confirmPassword, referenceName,  phone
                    )
                    signUpViewModel.userRegistration(userRegisterModel)

                    observe(signUpViewModel.userRegisterationLiveData) {

                        if (!it.consumed) it.consume()?.let {
                            if (it != null) {
                                showAlert("Alert!", "Registered Successfully.", "OK", null) {
                                    findNavController().navigate(R.id.adsHomeFragment)
                                    val homeActivity = requireActivity() as HomeActivity
                                    homeActivity.recreate()
                                }
                            }
                        }
                    }
                }
                setupProgressDialog(signUpViewModel.showHideProgressDialog, dialogHelper)
            }

        }

    }

    private fun isPasswordMatching(password: String?, confirmPassword: String?): Boolean {
        val pattern: Pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(confirmPassword)
        return if (!matcher.matches()) {
            binding.fragmentSignUpConfirmPasswordEd.error = "Your password is not matched."
            false
        } else true
    }

    private fun backButton() {
        binding.fragmentSignUpBackTv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun loadSpinnerData() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.vendor_array,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.fragmentSignUpSpinner.adapter = adapter
        binding.fragmentSignUpSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                heardAboutUsFrom = parent.getItemAtPosition(position)
                Log.i("spinner", "$selectedItem is Selected.")
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

}