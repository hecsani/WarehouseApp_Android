package com.example.warehouseapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouseapp.R
import com.example.warehouseapp.adapter.StoreAdapter
import com.example.warehouseapp.data.StoreItem
import com.example.warehouseapp.data.StoreListDatabase
import com.example.warehouseapp.databinding.ActivityListBinding
import com.example.warehouseapp.fragments.NewStoreItemDialogFragment
import kotlin.concurrent.thread
import com.example.warehouseapp.helper.*


class ListActivity : AppCompatActivity(), StoreAdapter.StoreItemClickListener,
    NewStoreItemDialogFragment.NewStoreItemDialogListener {

    private lateinit var binding: ActivityListBinding
    private lateinit var database: StoreListDatabase
    private lateinit var adapter: StoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        database = StoreListDatabase.getDatabase(applicationContext)

        binding.fab.setOnClickListener {
            NewStoreItemDialogFragment().show(
                supportFragmentManager,
                NewStoreItemDialogFragment.TAG
            )
        }

        initRecyclerView()

        val swipeToDeleteCallback = object: SwipeToDeleteCallback(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.deleteByPosition(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvMain)

    }

    override fun onItemChanged(item: StoreItem) {
        thread {
            database.storeItemDao().update(item)
            Log.d("ListActivity", "StoreItem update was successful")
        }
    }

    override fun onItemDeleted(item: StoreItem) {
        thread {
            database.storeItemDao().deleteItem(item)
            Log.d("Main Activity", "StoreItem successfully deleted")
            runOnUiThread {
                adapter.deleteItem(item)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = StoreAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.storeItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onStoreItemCreated(newItem: StoreItem) {
        thread {
            val insertId = database.storeItemDao().insert(newItem)
            newItem.id = insertId
            runOnUiThread {
                adapter.addItem(newItem)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.sort_category -> {
                adapter.sortByCategory()
                true
            }
            R.id.sort_availability -> {
                adapter.sortByAvailability()
                true
            }
            R.id.delete_all -> {
                adapter.deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}