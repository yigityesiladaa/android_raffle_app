package com.kimkazandi.libraries.room.repositories

import com.kimkazandi.libraries.room.dao.RaffleDao
import com.kimkazandi.models.Raffle

class RoomRaffleRepository {

    private var _raffleDao: RaffleDao? = null

    fun setRaffleDao(raffleDao: RaffleDao) {
        _raffleDao = raffleDao
    }


    companion object {
        private var raffleRepoInstance: RoomRaffleRepository? = null

        @Synchronized
        fun getRaffleRepoInstance() = raffleRepoInstance ?: synchronized(this) {
            raffleRepoInstance ?: RoomRaffleRepository().also { raffleRepoInstance = it }
        }
    }

    fun insert(raffle: Raffle) = _raffleDao?.insert(raffle)
    fun getAllByGroup(groupName: String) = _raffleDao?.getAllByGroup(groupName)
    fun isExist(href: String) = _raffleDao?.isExist(href)
    fun getRaffleByHref(href: String) = _raffleDao?.getRaffleByHref(href)
    fun clearAllRaffles() = _raffleDao?.clearAllRaffles()
}