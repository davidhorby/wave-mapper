package com.dhorby.wavemapper.wavemapper

import com.dhorby.wavemapper.wavemapper.Constants.MET_OFFICE_URL
import com.dhorby.wavemapper.wavemapper.Constants.metOfficeApiKey
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.net.URI

object WaveServiceFunctions : AppFunctions {
    private val xmlMapper = XmlMapper()

    private val setListUrls = URI(Constants.siteListUrl).toURL()

    override val siteListFunction: SiteListFunction = {
        val xmlText = setListUrls.readText()
        val readTree = xmlMapper.readTree(xmlText)
        readTree.getSiteLocations().toMutableList()
    }

    override val dataForSiteFunction: DataForSiteFunction = { site ->
        try {
            val metOfficeUrls =
                URI("${MET_OFFICE_URL}$site?res=3hourly&key=$metOfficeApiKey").toURL()
            val xmlText = metOfficeUrls.readText()
            xmlMapper.readTree(xmlText).getLocation()
        } catch (ex: Exception) {
            println("Failed to read url ${URI(MET_OFFICE_URL).toURL()} ${ex.message}")
            null
        }
    }
}
