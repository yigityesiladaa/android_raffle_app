package com.kimkazandi.libraries.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kimkazandi.models.Raffle

@Dao
interface RaffleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(raffle : Raffle) : Long

    @Query("select * from raffle where id =:id")
    fun getById(id: Int) : Raffle

    @Query("Select * from raffle where group_name like :groupName")
    fun getAllByGroup(groupName: String) : List<Raffle>

    @Query("SELECT COUNT(*) FROM raffle WHERE href = :href")
    fun isExist(href: String): Int

    @Query("select * from raffle where href = :href")
    fun getRaffleByHref(href: String): Raffle

    @Query("delete from raffle")
    fun clearAllRaffles()
}