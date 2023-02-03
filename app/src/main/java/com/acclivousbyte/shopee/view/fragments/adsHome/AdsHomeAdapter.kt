package com.acclivousbyte.shopee.view.fragments.adsHome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.models.generalAds.GeneralAdsData
import com.acclivousbyte.shopee.models.premiumAds.PremiumAdsData
import com.bumptech.glide.Glide

class AdsHomeAdapter(
    private val context: Context,
    private val onClick: (PremiumAdsData) -> Unit
): RecyclerView.Adapter<AdsHomeAdapter.AdsHomeViewHolder>() {


    private val differ = AsyncListDiffer(
        this,
        AdsHomeAdapter.DiffUtils
    )
    var items: List<PremiumAdsData> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<PremiumAdsData>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsHomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ads_home_items_view, parent, false)
        return AdsHomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdsHomeViewHolder, position: Int) {
        differ.currentList[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class AdsHomeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(data: PremiumAdsData) = itemView.run {

            Glide.with(context).load(data.images[0])
                .into(this.findViewById(R.id.ads_home_items_view_ads_image_iv))

            setOnClickListener {
                onClick.invoke(data)
            }
        }

    }

    private object DiffUtils : DiffUtil.ItemCallback<PremiumAdsData>() {
        override fun areItemsTheSame(
            oldItem: PremiumAdsData, newItem: PremiumAdsData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PremiumAdsData, newItem: PremiumAdsData
        ): Boolean {
            return oldItem == newItem
        }
    }
}