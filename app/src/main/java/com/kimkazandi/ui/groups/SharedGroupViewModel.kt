package com.kimkazandi.ui.groups

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kimkazandi.R
import com.kimkazandi.bots.TimeCheckerBot
import com.kimkazandi.common.states.BaseState
import com.kimkazandi.libraries.jSoup.repositories.JSoupRaffleRepository
import com.kimkazandi.libraries.room.dao.FollowsDao
import com.kimkazandi.libraries.room.dao.RaffleDao
import com.kimkazandi.libraries.room.repositories.RoomFollowsRepository
import com.kimkazandi.libraries.room.repositories.RoomRaffleRepository
import com.kimkazandi.models.Raffle

class SharedGroupViewModel(application: Application) : AndroidViewModel(application) {
    private val _jSoupRafflesRepository = JSoupRaffleRepository.getJSoupRaffleRepoInstance()
    private val _roomRaffleRepository = RoomRaffleRepository.getRaffleRepoInstance()
    private val _roomFollowsRepository = RoomFollowsRepository.getFollowsRepoInstance()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    private var _context: Context? = null
    var raffleCards = MutableLiveData<List<Raffle>>()
    private val timeCheckerBot = TimeCheckerBot(application)

    fun checkAndUpdateData(groupName: String) {
        timeCheckerBot.checkAndUpdateData {
            Thread {
                clearRoom()
                raffleCards.postValue(listOf())
                getAllRaffles(groupName)
            }.start()
        }

    }

    fun setContext(context: Context) {
        _context = context
    }

    fun setDao(raffleDao: RaffleDao, followsDao: FollowsDao) {
        _roomRaffleRepository.setRaffleDao(raffleDao)
        _roomFollowsRepository.setFollowsDao(followsDao)
    }

    private fun clearRoom() {
        _roomRaffleRepository.clearAllRaffles()
        _roomFollowsRepository.clearAllFollowedRaffles()
        _state.postValue(BaseState.Loading)
    }

    fun getAllRaffles(groupName: String) {
        raffleCards.postValue(listOf())
        val raffles = _roomRaffleRepository.getAllByGroup(groupName)
        if (raffles != null && raffles.isNotEmpty()) {
            raffleCards.postValue(raffles!!)
            _state.postValue(BaseState.Success())
        } else {
            getByGroup(groupName)
        }
    }

    private fun getByGroup(groupId: String) {
        val result = _jSoupRafflesRepository.getAllByGroup(groupId)
        if (result.isNotEmpty()) {
            raffleCards.postValue(result)
            insertToRoom(result)
            _state.postValue(BaseState.Success())
        } else {
            _state.postValue(BaseState.Error(_context?.getString(R.string.something_went_wrong_text)))
        }
    }

    private fun insertToRoom(raffles: List<Raffle>) {
        for (raffle in raffles) {
            raffle.href?.let {
                val isExist = _roomRaffleRepository.isExist(it)
                if (isExist != null && isExist == 0) {
                    _roomRaffleRepository.insert(raffle)
                }
            }
        }
    }

}