package com.dhorby.wavemapper.external.metoffice

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.Site
import com.dhorby.gcloud.wavemapper.Constants
import com.dhorby.gcloud.wavemapper.Constants.metOfficeApiKey
import com.dhorby.gcloud.wavemapper.Constants.metOfficeUrl
import com.dhorby.gcloud.wavemapper.getLocation
import com.dhorby.gcloud.wavemapper.getSiteLocations
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

    fun getSiteList(): MutableList<Location> {
        return siteList().map { site ->
            try {
                val metOfficeUrls =
                    URI("${metOfficeUrl}${site.id}?res=3hourly&key=${metOfficeApiKey}").toURL()
                val xmlText = metOfficeUrls.readText()
                xmlMapper.readTree(xmlText).getLocation()
            } catch (ex: Exception) {
                println("Failed to read url ${URI(metOfficeUrl).toURL()} ${ex.message}")
                null
            }
        }.filterNotNull().toMutableList()
    }

}