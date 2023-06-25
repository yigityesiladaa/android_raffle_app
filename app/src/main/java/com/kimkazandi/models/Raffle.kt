package com.kimkazandi.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "raffle")
data class Raffle(
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo("title") var title : String? = null,
    @ColumnInfo("description") var description : String? = null,
    @ColumnInfo("image_url") var imageUrl : String? = null,
    @ColumnInfo("start_date") var startDate : String? = null,
    @ColumnInfo("last_join_date") var lastJoinDate : String? = null,
    @ColumnInfo("total_gift_count") var totalGiftCount : String? = null,
    @ColumnInfo("raffle_date") var raffleDate : String? = null,
    @ColumnInfo("min_spend") var minSpend : String? = null,
    @ColumnInfo("total_gift_amount") var totalGiftAmount : String? = null,
    @ColumnInfo("advert_date") var advertDate : String? = null,
    @ColumnInfo("group_name") var groupName: String? = null,
    @ColumnInfo("href") var href: String? = null,
)

