package com.example.warehouseapp.activities


import android.graphics.Color.BLACK
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.warehouseapp.R
import com.example.warehouseapp.data.StoreItem
import com.example.warehouseapp.data.StoreListDatabase
import com.example.warehouseapp.databinding.ActivityStatBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.concurrent.thread


class StatActivity : AppCompatActivity() {

    private lateinit var database: StoreListDatabase
    private lateinit var binding: ActivityStatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = StoreListDatabase.getDatabase(applicationContext)
        createPieChart()
    }

    private fun createPieChart() {
        val chartData = mutableListOf<PieEntry>()
        val pieChart: PieChart = findViewById(R.id.chartCategory)

        thread {
            val data = database.storeItemDao().getAll()

            var numOfFurniture = 0F
            var numOfConst = 0F
            var numOfElectronic = 0F
            var numOfOther = 0F
            for (item in data) {
                when (item.category) {
                    StoreItem.Category.FURNITURE -> {
                        numOfFurniture++
                    }
                    StoreItem.Category.CONSTRUCTION_ITEM -> {
                        numOfConst++
                    }
                    StoreItem.Category.ELECTRONIC -> {
                        numOfElectronic++
                    }
                    StoreItem.Category.OTHER -> {
                        numOfOther++
                    }
                }
            }

            chartData.add(PieEntry(numOfFurniture, "Furniture"))
            chartData.add(PieEntry(numOfElectronic, "Electronic"))
            chartData.add(PieEntry(numOfConst, "Construction item"))
            chartData.add(PieEntry(numOfOther, "Other"))
        }

        val pieDataSet = PieDataSet(chartData, "Categories")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toMutableList()
        pieDataSet.valueTextColor = BLACK
        pieDataSet.valueTextSize = 16f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Categories"
        pieChart.animate()
    }
}