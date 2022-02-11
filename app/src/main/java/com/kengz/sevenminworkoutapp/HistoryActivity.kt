package com.kengz.sevenminworkoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kengz.sevenminworkoutapp.databinding.ActivityHistoryBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarHistoryActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "HISTORY"

        binding.toolbarHistoryActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        val historyDao = (application as WorkoutApp).db.historyDao()
        getAllCompletedDates(historyDao)

    }

    private fun getAllCompletedDates(historyDao: HistoryDao) {
        lifecycleScope.launch {
            historyDao.fetchAllDates().collect { allCompletedDatesList ->
                if (allCompletedDatesList.isNotEmpty()) {
                    binding.tvHistory.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.VISIBLE
                    binding.tvNoDataAvailable.visibility = View.INVISIBLE

                    binding.rvHistory.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    val dates = ArrayList<String>()
                    for (date in allCompletedDatesList) {
                        dates.add(date.date)
                    }
                    val historyAdapter = HistoryAdapter(dates)
                    binding.rvHistory.adapter = historyAdapter
                } else {
                    binding.tvHistory.visibility = View.INVISIBLE
                    binding.rvHistory.visibility = View.INVISIBLE
                    binding.tvNoDataAvailable.visibility = View.VISIBLE

                }
            }
        }

    }
}
