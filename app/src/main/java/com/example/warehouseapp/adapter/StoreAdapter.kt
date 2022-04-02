package com.example.warehouseapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseapp.R
import com.example.warehouseapp.data.StoreItem
import com.example.warehouseapp.databinding.ItemStoreListBinding

class StoreAdapter(private val listener: StoreItemClickListener) :
    RecyclerView.Adapter<StoreAdapter.StoreViewHolder>() {

    private val items = mutableListOf<StoreItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StoreViewHolder(
        ItemStoreListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        val storeItem = items[position]

        holder.binding.ivIcon.setImageResource(getImageResource(storeItem.category))
        holder.binding.cbIsBought.isChecked = !storeItem.isBought
        holder.binding.tvName.text = storeItem.name
        holder.binding.tvDescription.text = storeItem.description
        holder.binding.tvCategory.text = storeItem.category.name
        holder.binding.tvPrice.text = "${storeItem.price} Ft"

        holder.binding.cbIsBought.setOnCheckedChangeListener { buttonView, isChecked ->
            storeItem.isBought = isChecked
            listener.onItemChanged(storeItem)
        }

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemDeleted(storeItem)
        }

    }

    @DrawableRes()
    private fun getImageResource(category: StoreItem.Category): Int {
        return when (category) {
            StoreItem.Category.FURNITURE -> R.drawable.sofa
            StoreItem.Category.ELECTRONIC -> R.drawable.light
            StoreItem.Category.CONSTRUCTION_ITEM -> R.drawable.brick
            StoreItem.Category.OTHER -> R.drawable.cog
        }
    }

    fun addItem(item: StoreItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(storeItems: List<StoreItem>) {
        items.clear()
        items.addAll(storeItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: StoreItem) {
        items.remove(item)
        notifyDataSetChanged()
    }

    fun deleteAll(){
        for (item in items ){
            listener.onItemDeleted(item)
        }
    }


    fun deleteByPosition(pos: Int){
        listener.onItemDeleted(items[pos])
        notifyDataSetChanged()
    }

    fun sortByCategory() {
        items.sortBy { it.category }
        notifyDataSetChanged()
    }

    fun sortByAvailability() {
        items.sortBy { it.isBought }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size


    interface StoreItemClickListener {
        fun onItemChanged(item: StoreItem)
        fun onItemDeleted(item: StoreItem)
    }

    inner class StoreViewHolder(val binding: ItemStoreListBinding) : RecyclerView.ViewHolder(binding.root)
}
