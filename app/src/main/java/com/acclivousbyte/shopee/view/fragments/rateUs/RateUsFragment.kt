package com.acclivousbyte.shopee.view.fragments.rateUs

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acclivousbyte.shopee.R

class RateUsFragment : Fragment() {

    companion object {
        fun newInstance() = RateUsFragment()
    }

    private lateinit var viewModel: RateUsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rate_us, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RateUsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}