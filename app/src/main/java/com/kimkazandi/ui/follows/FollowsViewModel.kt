package com.kimkazandi.ui.follows

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kimkazandi.common.states.BaseState
import com.kimkazandi.libraries.room.dao.FollowsDao
import com.kimkazandi.libraries.room.dao.RaffleDao
import com.kimkazandi.libraries.room.repositories.RoomFollowsRepository
import com.kimkazandi.libraries.room.repositories.RoomRaffleRepository
import com.kimkazandi.models.Raffle

class FollowsViewModel : ViewModel() {
    private val _roomRaffleRepository = RoomRaffleRepository.getRaffleRepoInstance()
    private val _roomFollowsRepository = RoomFollowsRepository.getFollowsRepoInstance()
    var raffles = MutableLiveData<MutableList<Raffle>>()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    private var _context : Context? = null

    fun setContext(context : Context){
        _context = context
    }

    fun setDao(raffleDao: RaffleDao, followsDao: FollowsDao){
        _roomRaffleRepository.setRaffleDao(raffleDao)
        _roomFollowsRepository.setFollowsDao(followsDao)
    }

    fun getFollowedRaffles() {
        val raffleList = mutableListOf<Raffle>()
        _roomFollowsRepository.getFollowedRaffles()?.forEach { followedRaffle ->
            followedRaffle.let {
                it.raffleHref?.let { href->
                    val raffle = getRaffleByHref(href)
                    if(raffle != null){
                        raffleList.add(raffle)
                        _state.postValue(BaseState.Success())
                    }else{
                        _state.postValue(BaseState.Error())
                    }
                }
            }
        }
        raffles.postValue(raffleList)
    }

    private fun getRaffleByHref(href: String): Raffle?{
        val followedRaffle = _roomFollowsRepository.getFollowedRaffleByHref(href)
        followedRaffle?.let { fr->
            if(fr.raffleHref != null){
                return _roomRaffleRepository.getRaffleByHref(fr.raffleHref)

            }
        }
        return null
    }
}