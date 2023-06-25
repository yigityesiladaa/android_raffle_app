package com.kimkazandi.bots

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.concurrent.TimeUnit

class TimeCheckerBot(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("DataUpdater", Context.MODE_PRIVATE)
    private val lastUpdateTimeKey = "lastUpdateTime"
    private val updateInterval: Long = TimeUnit.HOURS.toMillis(3)

    fun checkAndUpdateData(fetchDataAndUpdate: () -> Unit){
        val currentTime = System.currentTimeMillis()
        val lastUpdateTime = sharedPreferences.getLong(lastUpdateTimeKey, 0)
        if (currentTime - lastUpdateTime >= updateInterval) {
            fetchDataAndUpdate()
            sharedPreferences.edit {
                putLong(lastUpdateTimeKey, currentTime)
                apply()
            }
        }
    }
}