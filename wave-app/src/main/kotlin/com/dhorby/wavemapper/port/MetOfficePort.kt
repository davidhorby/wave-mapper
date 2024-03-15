package com.dhorby.wavemapper.port

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.Site
import com.dhorby.gcloud.model.WaveLocation
import com.dhorby.gcloud.wavemapper.Constants
import com.dhorby.gcloud.wavemapper.getLocation
import com.dhorby.gcloud.wavemapper.getMetOfficeUrl
import com.dhorby.gcloud.wavemapper.getSiteLocations
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.net.URI

abstract class MetOfficePort() {

    private val setListUrls = URI(Constants.siteListUrl).toURL()
    private val xmlMapper = XmlMapper()

    private fun getSiteListFromMetOffice(): MutableList<Site> {
        val xmlText = setListUrls.readText()
        val readTree = xmlMapper.readTree(xmlText)
        return readTree.getSiteLocations().toMutableList()
    }

    fun waveLocations(): MutableList<Location> {
        val buoyLocations =  getSiteListFromMetOffice().mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        val mapNotNull: List<WaveLocation> = buoyLocations.mapNotNull { site ->
            dataForSiteFunction(site.id)
        }
        return mapNotNull.filter { location ->
            location.id.isNotEmpty()
        }.toMutableList()
    }

    private fun dataForSiteFunction(siteId:String): WaveLocation? {
        return try {
            val metOfficeUrls = URI(getMetOfficeUrl(siteId)).toURL()
            val xmlText = metOfficeUrls.readText()
            xmlMapper.readTree(xmlText).getLocation()
        } catch (ex: Exception) {
            println("Failed to read url ${URI(Constants.metOfficeUrl).toURL()} ${ex.message}")
            null
        }
    }
}