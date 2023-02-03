package com.acclivousbyte.shopee.view.fragments.brands

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.acclivousbyte.shopee.R
import com.acclivousbyte.shopee.databinding.BrandsItmeViewBinding
import com.acclivousbyte.shopee.models.brands.BrandsDetails
import com.bumptech.glide.Glide

class BrandsAdapter(
    private val context: Context,
    private val onClick:(BrandsDetails) -> Unit
): RecyclerView.Adapter<BrandsAdapter.MViewAdapter>(){

    private val differ = AsyncListDiffer(
        this,
        DiffUtils
    )
    var items: List<BrandsDetails> = emptyList()
        set(value) {
            field = value
            differ.submitList(ArrayList(value))
        }

    init {
        differ.submitList(items)
    }

    fun submitList(list: List<BrandsDetails>) {
        differ.submitList(arrayListOf())
        items = list
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewAdapter {
//        val view =LayoutInflater.from(parent.context)
//            .inflate(R.layout.brands_itme_view, parent, false)
        val binding = BrandsItmeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MViewAdapter(binding)
        //return MViewAdapter(view)
    }

    override fun onBindViewHolder(holder: MViewAdapter, position: Int) {
       differ.currentList[position].let {
            holder.bind2(it)
       }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MViewAdapter(private val binding: BrandsItmeViewBinding): RecyclerView.ViewHolder(binding.root) {

//        fun bind(model: BrandsDetails) = itemView.run {
//            this.findViewById<ImageView>(R.id.brands_item_view_iv).run {
//
//                Glide.with(context).load(model.logo_url).into(this)
//
//                setOnClickListener {
//                    onClick.invoke(model)
//                }
//            }
//        }

        fun bind2(data: BrandsDetails) = binding.run {
            Glide.with(binding.brandsItemViewIv).load(data.logo_url)
                .into(binding.brandsItemViewIv)

            this.cardView.setOnClickListener {
                onClick.invoke(data)
            }
        }
    }

    object DiffUtils : DiffUtil.ItemCallback<BrandsDetails>() {
        override fun areItemsTheSame(
            oldItem: BrandsDetails, newItem: BrandsDetails
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BrandsDetails, newItem: BrandsDetails
        ): Boolean {
            return oldItem == newItem
        }
    }

}
