package com.example.warehouseapp.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.example.warehouseapp.R
import com.example.warehouseapp.data.StoreItem
import com.example.warehouseapp.databinding.DialogNewStoreItemBinding


class NewStoreItemDialogFragment : DialogFragment() {
    interface NewStoreItemDialogListener {
        fun onStoreItemCreated(newItem: StoreItem)
    }

    private lateinit var listener: NewStoreItemDialogListener

    private lateinit var binding: DialogNewStoreItemBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewStoreItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewStoreItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogNewStoreItemBinding.inflate(LayoutInflater.from(context))
        binding.spCategory.adapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.category_items)
        )

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_store_item)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onStoreItemCreated(getStoreItem())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    private fun isValid() = binding.etName.text.isNotEmpty()

    private fun getStoreItem() = StoreItem(
        name = binding.etName.text.toString(),
        description = binding.etDescription.text.toString(),
        price = binding.etPrice.text.toString().toIntOrNull() ?: 0,
        category = StoreItem.Category.getByOrdinal(binding.spCategory.selectedItemPosition)
            ?: StoreItem.Category.OTHER,
        isBought = binding.cbAlreadyPurchased.isChecked
    )


    companion object {
        const val TAG = "NewStoreItemDialogFragment"
    }
}
