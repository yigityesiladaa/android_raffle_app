package com.kimkazandi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followedRaffle")
data class FollowedRaffle(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "raffle_href") val raffleHref: String?,
)
