package com.kimkazandi.libraries.room.repositories

import com.kimkazandi.libraries.room.dao.FollowsDao
import com.kimkazandi.models.FollowedRaffle

class RoomFollowsRepository {
    private var _followsDao: FollowsDao? = null

    fun setFollowsDao(followsDao: FollowsDao) {
        _followsDao = followsDao
    }

    companion object {
        private var followsRepoInstance: RoomFollowsRepository? = null

        @Synchronized
        fun getFollowsRepoInstance() = followsRepoInstance ?: synchronized(this) {
            followsRepoInstance ?: RoomFollowsRepository().also { followsRepoInstance = it }
        }
    }

    fun getFollowedRaffles() = _followsDao?.getAllRaffles()
    fun followRaffle(followedRaffle: FollowedRaffle) = _followsDao?.follow(followedRaffle)
    fun unfollowRaffle(raffleHref: String) = _followsDao?.unfollow(raffleHref)
    fun isFollowedRaffle(raffleHref: String) = _followsDao?.isExist(raffleHref)
    fun getFollowedRaffleByHref(raffleHref: String) = _followsDao?.getRaffleByHref(raffleHref)
    fun clearAllFollowedRaffles() = _followsDao?.clearAllFollowedRaffles()
}