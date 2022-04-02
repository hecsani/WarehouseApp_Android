package com.example.warehouseapp.data

import androidx.room.*

@Dao
interface StoreItemDao {
    @Query("SELECT * FROM storeitem")
    fun getAll(): List<StoreItem>

    @Insert
    fun insert(storeItems: StoreItem): Long

    @Update
    fun update(storeItem: StoreItem)

    @Delete
    fun deleteItem(storeItem: StoreItem)
}
