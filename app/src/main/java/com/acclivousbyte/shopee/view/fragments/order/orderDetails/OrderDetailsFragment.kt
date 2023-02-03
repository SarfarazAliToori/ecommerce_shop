package com.acclivousbyte.shopee.view.fragments.order.orderDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.acclivousbyte.shopee.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {


    private lateinit var binding: FragmentOrderDetailsBinding
    private lateinit var adapter: OrderDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        val dummyList = listOf("800", "950", "750", "1200", "1000")
        //adapter = OrderDetailAndSummaryAdapter {  }
        val recyclerView = binding.fragmentOrderDetailsRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        //adapter.submitList(dummyList)


        return binding.root
    }

}