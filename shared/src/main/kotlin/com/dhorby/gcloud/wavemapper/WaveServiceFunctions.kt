package com.dhorby.gcloud.wavemapper

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
            val metOfficeUrls = URI(getMetOfficeUrl(site)).toURL()
            val xmlText = metOfficeUrls.readText()
            xmlMapper.readTree(xmlText).getLocation()
        } catch (ex: Exception) {
            println("Failed to read url ${URI(Constants.metOfficeUrl).toURL()} ${ex.message}")
            null
        }
    }
}
