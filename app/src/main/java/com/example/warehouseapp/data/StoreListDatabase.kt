package com.example.warehouseapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [StoreItem::class], version = 1)
@TypeConverters(value = [StoreItem.Category::class])
abstract class StoreListDatabase : RoomDatabase() {
    abstract fun storeItemDao(): StoreItemDao

    companion object {
        fun getDatabase(applicationContext: Context): StoreListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                StoreListDatabase::class.java,
                "store-list"
            ).build();
        }
    }
}
