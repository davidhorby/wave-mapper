package com.dhorby.wavemapper.external.metoffice

import com.dhorby.wavemapper.model.Location
import com.dhorby.wavemapper.model.Site
import com.dhorby.wavemapper.wavemapper.Constants
import com.dhorby.wavemapper.wavemapper.Constants.MET_OFFICE_URL
import com.dhorby.wavemapper.wavemapper.Constants.metOfficeApiKey
import com.dhorby.wavemapper.wavemapper.getLocation
import com.dhorby.wavemapper.wavemapper.getSiteLocations
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.net.URI

class MetOfficeClient {
    private val xmlMapper = XmlMapper()

    private val setListUrls = URI(Constants.siteListUrl).toURL()

    private fun siteList(): MutableList<Site> {
        val xmlText = setListUrls.readText()
        val readTree = xmlMapper.readTree(xmlText)
        return readTree.getSiteLocations().toMutableList()
    }

    fun getSiteList(): MutableList<Location> =
        siteList()
            .map { site ->
                try {
                    val metOfficeUrls =
                        URI("${MET_OFFICE_URL}${site.id}?res=3hourly&key=$metOfficeApiKey").toURL()
                    val xmlText = metOfficeUrls.readText()
                    xmlMapper.readTree(xmlText).getLocation()
                } catch (ex: Exception) {
                    println("Failed to read url ${URI(MET_OFFICE_URL).toURL()} ${ex.message}")
                    null
                }
            }.filterNotNull()
            .toMutableList()
}
