package com.kimkazandi.listeners

import com.kimkazandi.models.Raffle

interface ISharedGroupListener {
    fun onItemClickListener(raffle: Raffle)
}