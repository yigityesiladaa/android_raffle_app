package com.kimkazandi.libraries.jSoup.services

import com.kimkazandi.models.Raffle

interface IRaffleService {
    fun getAllByGroup(groupUrl: String): List<Raffle>
    fun getRaffleByHref(href: String, groupName: String): Raffle?
}