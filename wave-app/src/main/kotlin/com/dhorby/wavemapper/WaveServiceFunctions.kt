package com.dhorby.wavemapper

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.net.URL

class WaveServiceFunctions:AppFunctions {

    private val xmlMapper = XmlMapper()

    override val siteListFunction: SiteListFunction = {
        val xmlText = URL(Constants.siteListUrl).readText()
        xmlMapper.readTree(xmlText).getSiteLocations().toMutableList()
    }

    override val dataForSiteFunction: DataForSiteFunction = { site ->
        try {
            val xmlText = URL(getMetOfficeUrl(site)).readText()
            xmlMapper.readTree(xmlText).getLocation()
        } catch (ex: Exception) {
            println("Failed to read url ${URL(Constants.metOfficeUrl)} ${ex.message}")
            null
        }
    }
}