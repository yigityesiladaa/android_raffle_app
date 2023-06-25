package com.kimkazandi.libraries.jSoup.repositories

import com.kimkazandi.constants.Constants
import com.kimkazandi.libraries.jSoup.services.IRaffleService
import com.kimkazandi.models.Raffle
import com.kimkazandi.security.Security
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class JSoupRaffleRepository : IRaffleService {

    companion object {
        private var jSoupRaffleRepoInstance: JSoupRaffleRepository? = null

        @Synchronized
        fun getJSoupRaffleRepoInstance() = jSoupRaffleRepoInstance ?: synchronized(this) {
            jSoupRaffleRepoInstance ?: JSoupRaffleRepository().also { jSoupRaffleRepoInstance = it }
        }
    }

    override fun getAllByGroup(groupName: String): List<Raffle> {
        val arr = mutableListOf<Raffle>()
        val document = getDocument(Constants.RAFFLES_URL + groupName)
        val cardElements = document.getElementsByClass("col-sm-3 col-lg-3 item")

        for (cardElement in cardElements) {
            val href = cardElement.selectFirst(".img a")?.attr("abs:href")?.split("/")?.last()
            val title = cardElement.select("h4").text()
            val imageUrl = cardElement.select("img").attr("src")
            val lastJoinDate = cardElement.select(".date.d-flex:nth-child(1)").text()
            val minSpend = cardElement.select(".date.kosul_fiyat.d-flex").text()
            val totalGiftAmount = cardElement.select(".date.d-flex:nth-child(2)").text()
            if (
                href != null &&
                title.isNotEmpty() &&
                imageUrl.isNotEmpty() &&
                lastJoinDate.isNotEmpty() &&
                minSpend.isNotEmpty() &&
                totalGiftAmount.isNotEmpty()
            ) {
                val raffle = Raffle(
                    title = title,
                    imageUrl = Constants.BASE_URL + imageUrl,
                    lastJoinDate = lastJoinDate,
                    minSpend = minSpend,
                    totalGiftAmount = totalGiftAmount,
                    groupName = groupName,
                    href = href
                )
                arr.add(raffle)
            }
        }
        return arr
    }


    override fun getRaffleByHref(href: String, groupName: String): Raffle? {
        val document = getDocument(Constants.RAFFLE_DETAIL_URL + href)

        val element = document.selectFirst(".campaignDetail")
        var description = ""
        val title = element?.getElementsByTag("h1")?.firstOrNull()?.text()
        element?.getElementsByClass("scrollbar-dynamic")?.forEach {
            description += "\n\n" + it.getElementsByTag("p").text()
        }

        val imageUrl = element?.getElementsByAttribute("alt")?.firstOrNull()?.attr("src")
        val startDate =
            element?.selectFirst("label:contains(Başlangıç Tarihi)")?.parent()?.text()?.substringAfter(":")?.trim()
        val lastJoinDate =
            element?.selectFirst("label:contains(Son Katılım Tarihi)")?.parent()?.text()?.substringAfter(":")?.trim()
        val totalGiftCount =
            element?.selectFirst("label:contains(Toplam Hediye Sayısı)")?.parent()?.text()?.substringAfter(":")?.trim()
        val raffleDate =
            element?.selectFirst("label:contains(Çekiliş Tarihi)")?.parent()?.text()?.substringAfter(":")?.trim()
        val minSpend =
            element?.selectFirst("label:contains(Min. Harcama Tutarı)")?.parent()?.text()?.substringAfter(":")?.trim()
        val totalGiftAmount =
            element?.selectFirst("label:contains(Toplam Hediye Değeri)")?.parent()?.text()?.substringAfter(":")?.trim()
        val advertDate =
            element?.selectFirst("label:contains(İlan Tarihi)")?.parent()?.text()?.substringAfter(":")?.trim()

        if (imageUrl != null &&
            startDate != null &&
            lastJoinDate != null &&
            totalGiftCount != null &&
            raffleDate != null &&
            minSpend != null &&
            totalGiftAmount != null &&
            advertDate != null
        ) {
            return Raffle(
                title = title,
                description = description,
                imageUrl = Constants.BASE_URL + imageUrl,
                startDate = startDate,
                lastJoinDate = lastJoinDate,
                totalGiftCount = totalGiftCount,
                raffleDate = raffleDate,
                minSpend = minSpend,
                totalGiftAmount = totalGiftAmount,
                advertDate = advertDate,
                groupName = groupName,
                href = href
            )
        }
        return null
    }

    private fun getDocument(url: String): Document {
        return Jsoup.connect(url).method(Connection.Method.GET)
            .sslSocketFactory(Security.getSSLContext().socketFactory)
            .timeout(15000).get()
    }

}
