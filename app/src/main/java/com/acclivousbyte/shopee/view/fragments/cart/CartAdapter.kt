package com.acclivousbyte.shopee.view.fragments.cart

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
import com.acclivousbyte.shopee.models.CartData
import com.acclivousbyte.shopee.utils.AppUtils
import com.bumptech.glide.Glide

class CartAdapter(
    val context: Context,
    val listener: OnClickListener
): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    private var quantity: Int = 0
    private val differ = AsyncListDiffer<CartData>(
        this, DiffUtils
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_list_items_view, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(data: CartData) = itemView.run {
            this.findViewById<TextView>(R.id.cart_list_items_view_title).run {
                text = data.productsTitle
            }
            this.findViewById<TextView>(R.id.cart_list_items_view_subtitle).run {
                text = data.productsDescription
            }

            this.findViewById<TextView>(R.id.cart_list_items_view_discount_price).run {
                text = " " + data.productsPrice
            }

            Glide.with(context).load(data.productImage)
                .into(this.findViewById(R.id.cart_list_items_view_image))

            this.findViewById<TextView>(R.id.cart_list_items_quantity_tv).run {
                text = data.productQuantity.toString()
            }

//            this.findViewById<TextView>(R.id.cart_list_items_minus_tv).run {
//
//                setOnClickListener {
//                    listener.onMinus(absoluteAdapterPosition)
//                }
//            }

            this.findViewById<ImageView>(R.id.cart_list_items_minus_iv).run {
                setOnClickListener {
                    listener.onMinus(absoluteAdapterPosition)
                }
            }

//            this.findViewById<TextView>(R.id.cart_list_items_plus_tv).run {
//
//                setOnClickListener {
//                    listener.onPlus(absoluteAdapterPosition)
//                }
//            }

            this.findViewById<ImageView>(R.id.cart_list_items_plus_iv).run {
                setOnClickListener {
                    listener.onPlus(absoluteAdapterPosition)
                }
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

    interface OnClickListener {
        fun onPlus(position: Int)

        fun onMinus(position: Int)
    }

}