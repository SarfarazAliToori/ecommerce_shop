package com.acclivousbyte.shopee.view.fragments.order.orderDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.CartData
import com.bumptech.glide.Glide

class OrderDetailAdapter(
    val onClick: (CartData) -> Unit
): RecyclerView.Adapter<OrderDetailAdapter.OrderViewHolder>() {


    private val differ = AsyncListDiffer(
        this,
        DiffUtils
    )
    var items: List<CartData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<CartData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_summary_list_items_view, parent, false)
        return  OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class OrderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: CartData) = itemView.run {
            this.findViewById<TextView>(R.id.order_summar_list_items_view_price).run {
                text = data.productsPrice
            }

            this.findViewById<TextView>(R.id.order_summary_list_items_view_title).run {
                text = data.productsTitle
            }

            this.findViewById<TextView>(R.id.order_summary_list_items_view_subtitle).run {
                text = data.productsDescription
            }

            this.findViewById<TextView>(R.id.cart_list_items_quantity_tv).run {
                text = data.productQuantity.toString()
            }

            Glide.with(context).load(data.productImage)
                .into(this.findViewById(R.id.order_summary_list_items_image))

            setOnClickListener {
                onClick.invoke(data)
            }
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<CartData>() {
        override fun areItemsTheSame(
            oldItem: CartData, newItem: CartData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CartData, newItem: CartData
        ): Boolean {
            return oldItem == newItem
        }
    }
}