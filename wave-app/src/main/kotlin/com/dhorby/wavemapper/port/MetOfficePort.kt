package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.Site
import com.dhorby.gcloud.model.WaveLocation
import com.dhorby.gcloud.wavemapper.Constants
import com.dhorby.gcloud.wavemapper.Constants.metOfficeApiKey
import com.dhorby.gcloud.wavemapper.Constants.metOfficeUrl
import com.dhorby.gcloud.wavemapper.getLocation
import com.dhorby.gcloud.wavemapper.getSiteLocations
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.net.URI
import java.net.URL

abstract class MetOfficePort {
    private val setListUrls = URI(Constants.siteListUrl).toURL()
    private val xmlMapper = XmlMapper()

    private fun getSiteListFromMetOffice(): MutableList<Site> {
        val xmlText = setListUrls.readText()
        val readTree = xmlMapper.readTree(xmlText)
        return readTree.getSiteLocations().toMutableList()
    }

    fun waveLocations(): List<Location> {
        val siteListFromMetOffice: MutableList<Site> = getSiteListFromMetOffice()
        val buoyLocations: List<WaveLocation> =
            siteListFromMetOffice
                .filterNot {
                    it.id.isEmpty()
                }.map { dataForSiteFunction(it.id) }
                .filterNotNull()
        return buoyLocations
    }

    private fun dataForSiteFunction(siteId: String): WaveLocation? {
        val metOfficeUrl: URL =
            URI("${metOfficeUrl}$siteId?res=3hourly&key=$metOfficeApiKey").toURL()
        return try {
            val xmlText = metOfficeUrl.readText()
            xmlMapper.readTree(xmlText).getLocation()
        } catch (ex: Exception) {
            println("Failed to read url $metOfficeUrl ${ex.message}")
            null
        }
    }
}
