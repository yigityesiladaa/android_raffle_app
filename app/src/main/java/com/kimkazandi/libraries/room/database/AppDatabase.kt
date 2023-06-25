package com.kimkazandi.libraries.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kimkazandi.libraries.room.dao.FollowsDao
import com.kimkazandi.libraries.room.dao.RaffleDao
import com.kimkazandi.models.FollowedRaffle
import com.kimkazandi.models.Raffle

@Database(entities = [Raffle::class, FollowedRaffle::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun raffleDao() : RaffleDao
    abstract fun followsDao() : FollowsDao

}