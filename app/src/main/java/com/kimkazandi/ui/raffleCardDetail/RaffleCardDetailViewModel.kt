package com.kimkazandi.ui.raffleCardDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kimkazandi.common.states.BaseState
import com.kimkazandi.libraries.jSoup.repositories.JSoupRaffleRepository
import com.kimkazandi.libraries.room.repositories.RoomFollowsRepository
import com.kimkazandi.libraries.room.repositories.RoomRaffleRepository
import com.kimkazandi.models.FollowedRaffle
import com.kimkazandi.models.Raffle

class RaffleCardDetailViewModel : ViewModel() {
    private val _roomRaffleRepository = RoomRaffleRepository.getRaffleRepoInstance()
    private val _roomFollowsRepository = RoomFollowsRepository.getFollowsRepoInstance()
    private val _jsoupRaffleRepository = JSoupRaffleRepository.getJSoupRaffleRepoInstance()
    private val _state by lazy { MutableLiveData<BaseState>(BaseState.Loading) }
    val state: LiveData<BaseState> = _state
    private var _context: Context? = null
    var isFollowedRaffle = MutableLiveData<Boolean>()
    var raffleDetail = MutableLiveData<Raffle>()

    fun setContext(context: Context) {
        _context = context
    }

    fun initializeFollowStatus(href: String) {
        isFollowedRaffle.postValue(isFollowedRaffle(href))
    }

    fun getRaffleDetailByHref(href: String, groupName: String) {
        val roomResult = _roomRaffleRepository.getRaffleByHref(href)
        if (roomResult?.description != null) {
            raffleDetail.postValue(roomResult!!)
            _state.postValue(BaseState.Success())
        } else {
            val jsoupResult = _jsoupRaffleRepository.getRaffleByHref(href, groupName)
            if(jsoupResult != null){
                _state.postValue(BaseState.Success())
                jsoupResult.id = roomResult?.id
                val result = _roomRaffleRepository.insert(jsoupResult)
                if(result != null && result > 0){
                    raffleDetail.postValue(jsoupResult!!)
                }
            }else{
                _state.postValue(BaseState.Error())
            }
        }
    }

    fun changeFollowStatus(raffle: FollowedRaffle) {
        raffle.raffleHref?.let { raffleHref ->
            val followStatus = isFollowedRaffle(raffleHref)
            isFollowedRaffle.postValue(followStatus)
            if (followStatus) {
                unfollowRaffle(raffleHref)
            } else {
                followRaffle(raffle)
            }
        }
    }

    private fun followRaffle(raffle: FollowedRaffle) {
        val result = _roomFollowsRepository.followRaffle(raffle)
        result?.let {
            isFollowedRaffle.postValue(result > 0)
        }
    }

    private fun unfollowRaffle(raffleHref: String) {
        val result = _roomFollowsRepository.unfollowRaffle(raffleHref)
        result?.let {
            isFollowedRaffle.postValue(result <= 0)
        }
    }


    private fun isFollowedRaffle(raffleHref: String): Boolean {
        val result = _roomFollowsRepository.isFollowedRaffle(raffleHref)
        result?.let {
            isFollowedRaffle.postValue(it != 0)
            return it != 0
        }
        return false
    }
}