package com.acclivousbyte.shopee.view.fragments.order.orderListing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.placeOrder.ItemsDetails
import com.acclivousbyte.shopee.models.userOrders.UserOrdersData
import com.acclivousbyte.shopee.view.fragments.cart.CartAdapter
import org.w3c.dom.Text

class OrderListingAdapter(
    val onClick: () -> Unit
): RecyclerView.Adapter<OrderListingAdapter.OrderListingViewHolder>() {

    private val differ = AsyncListDiffer<UserOrdersData>(
        this, DiffUtils
    )

    var items: List<UserOrdersData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<UserOrdersData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.orders_list_items_view, parent, false)
        return OrderListingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderListingViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class OrderListingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(data: UserOrdersData) = itemView.run {
            this.findViewById<TextView>(R.id.order_list_items_view_date).run {
                text = data.created_at
            }
            this.findViewById<TextView>(R.id.orders_list_items_view_order_id).run {
                text = data.id.toString()
            }

            this.findViewById<TextView>(R.id.orders_list_items_view_total_items).run {
                data.items?.forEach {
                    text = "Total items ${it.quantity}"
                }
            }

            this.findViewById<TextView>(R.id.orders_list_items_view_price).run {
                text = data.total_price.toString()
            }

            setOnClickListener {
                onClick.invoke()
            }
        }
    }

    private object DiffUtils: DiffUtil.ItemCallback<UserOrdersData>() {
        override fun areItemsTheSame(oldItem: UserOrdersData, newItem: UserOrdersData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UserOrdersData, newItem: UserOrdersData): Boolean {
            return oldItem == newItem
        }

    }
}