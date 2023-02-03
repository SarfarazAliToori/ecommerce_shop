package com.acclivousbyte.shopee.view.fragments.promotionsList

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.promotionList.PromotionData
import com.acclivousbyte.shopee.models.promotionList.PromotionList
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

class PromotionListAdapter(
    private val context: Context,
    val onPromotionItemClick: (PromotionData) -> Unit,
): RecyclerView.Adapter<PromotionListAdapter.PromotionViewHolder>() {



    private val differ = AsyncListDiffer<PromotionData>(
        this, DiffUtils
    )

    var items: List<PromotionData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<PromotionData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.promotions_list_items_view, parent, false)
        return PromotionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
//        val dd = differ.currentList[position]
//        holder.bind(dd)
        // shortcut
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class PromotionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: PromotionData) = itemView.run {
            this.findViewById<TextView>(R.id.promotions_list_items_view_title).run {
                text = data.title
            }

            this.findViewById<TextView>(R.id.promotions_list_items_view_short_description).run {
                text = data.short_description
            }

            this.findViewById<TextView>(R.id.promotions_list_items_view_old_price).run {
                text = "RS,${data.old_price.toString()}"
                paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG;
            }

            this.findViewById<TextView>(R.id.promotions_list_items_view_price).run {
                text = data.price.toString()
            }

            Glide.with(context)
                .load(data.device_image).into(this.findViewById(R.id.promotions_list_items_view_image))

            setOnClickListener {
                onPromotionItemClick.invoke(data)
            }
        }
    }

    private object DiffUtils: DiffUtil.ItemCallback<PromotionData>() {
        override fun areItemsTheSame(oldItem: PromotionData, newItem: PromotionData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PromotionData, newItem: PromotionData): Boolean {
            return oldItem == newItem
        }

    }
}