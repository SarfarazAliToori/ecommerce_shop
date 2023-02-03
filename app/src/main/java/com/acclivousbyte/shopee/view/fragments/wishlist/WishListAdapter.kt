package com.acclivousbyte.shopee.view.fragments.wishlist

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.productsList.ProductsListData
import com.acclivousbyte.shopee.models.promotionList.PromotionData
import com.acclivousbyte.shopee.view.fragments.promotionsList.PromotionListAdapter
import com.bumptech.glide.Glide

class WishListAdapter(
    private val listener: WishListListener,
    private val onClick: (ProductsListData) -> Unit,
): RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {

    private val differ = AsyncListDiffer<ProductsListData>(
        this, DiffUtils
    )

    var items: List<ProductsListData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: MutableList<ProductsListData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_items_view, parent, false)
        return WishListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun deleteItem(index: Int) {
        differ.removeListListener { previousList, currentList ->
            previousList.removeAt(index)
            currentList.removeAt(index)
        }
        notifyItemRemoved(index)
    }

    inner class WishListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: ProductsListData) = itemView.run {
            this.findViewById<TextView>(R.id.products_items_view_title).run {
                text = data.title
            }

            this.findViewById<TextView>(R.id.products_items_view_description).run {
                text = data.description
            }

            this.findViewById<TextView>(R.id.products_items_view_price).run {
                text = data.rate.toString()
            }

            Glide.with(context)
                .load(data.logo).into(this.findViewById(R.id.products_items_view_logo_iv))

            this.setOnClickListener {
                onClick.invoke(data)
            }

            this.findViewById<LinearLayout>(R.id.products_items_view_add_to_cart).run {
                setOnClickListener {
                    listener.onClickAddToCart(data)
                }
            }
        }
    }

    private object DiffUtils: DiffUtil.ItemCallback<ProductsListData>() {
        override fun areItemsTheSame(oldItem: ProductsListData, newItem: ProductsListData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProductsListData, newItem: ProductsListData): Boolean {
            return oldItem == newItem
        }

    }
}

interface WishListListener {
    fun onClickAddToCart(productsListData: ProductsListData)
}