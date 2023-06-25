package com.kimkazandi.libraries.room.dao

import androidx.room.*
import com.kimkazandi.models.FollowedRaffle

@Dao
interface FollowsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun follow(followedRaffle: FollowedRaffle) : Long

    @Query("delete from followedRaffle where raffle_href =:raffleHref")
    fun unfollow(raffleHref: String): Int

    @Query("Select * from followedRaffle order by id DESC")
    fun getAllRaffles() : List<FollowedRaffle>

    @Query("SELECT COUNT(*) FROM followedRaffle WHERE raffle_href = :raffleHref")
    fun isExist(raffleHref: String): Int

    @Query("select * from followedRaffle where raffle_href = :raffleHref")
    fun getRaffleByHref(raffleHref: String): FollowedRaffle

    @Query("delete from followedRaffle")
    fun clearAllFollowedRaffles()
}