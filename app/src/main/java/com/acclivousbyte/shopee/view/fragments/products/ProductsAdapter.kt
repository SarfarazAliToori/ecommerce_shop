package com.acclivousbyte.shopee.view.fragments.products

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.productsList.ProductsListData
import com.bumptech.glide.Glide

class ProductsAdapter(
    private val context: Context,
    private val onClickListener: OnClickListener,
    private val onProductClick:(ProductsListData) -> Unit,
): RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {

    private val differ = AsyncListDiffer(
        this,
        DiffUtils
    )
    var items: List<ProductsListData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<ProductsListData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_items_view,parent, false)

        return ProductsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ProductsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

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
                .load(data.logo)
                .into(this.findViewById(R.id.products_items_view_logo_iv))


            this.findViewById<TextView>(R.id.products_items_view_add_to_cart_view).run {
                setOnClickListener {
//                    onCartAddToCartClick.invoke(data)
                    onClickListener.onAddToCartClick(data)
                }
            }
//             on item view click
            setOnClickListener {
                onProductClick.invoke(data)
            }
        }
    }

    private object DiffUtils : DiffUtil.ItemCallback<ProductsListData>() {
        override fun areItemsTheSame(
            oldItem: ProductsListData, newItem: ProductsListData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductsListData, newItem: ProductsListData
        ): Boolean {
            return oldItem == newItem
        }
    }
}

interface OnClickListener {
    fun onAddToCartClick(productsListData: ProductsListData)
}