package com.dhorby.wavemapper.handlers

import com.dhorby.gcloud.model.Location
import com.dhorby.wavemapper.*
import com.dhorby.wavemapper.Constants.mapsApiKey
import com.dhorby.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.model.Wave
import com.dhorby.wavemapper.model.WavePage
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.util.*

class WaveHandlers(val siteListFunction: SiteListFunction, val dataForSiteFunction:DataForSiteFunction) {

    private val devMode = false;

    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }

    fun getWavePage(): HttpHandler = {

        val viewModel: ViewModel =
            mapsApiKey.let { mapsApiKey ->
                val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction = siteListFunction, dataForSiteFunction)
                val withStoredSharks = allWaveData
                    .withStoredSharks()
                val waveData: String =
                    withStoredSharks
//                        .withBoat()
                        .toGoogleMapFormat()
                WavePage(waveData, mapsApiKey)
            }


        viewModel.let {
            try {
                Response(OK).body(renderer(viewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        }

    }

    fun getWaveData(): HttpHandler = {
        val allWaveData: MutableList<Location> = getAllWaveData(siteListFunction, dataForSiteFunction)
        Response(Status.OK).with(waveLocationListBodyLens() of allWaveData)
    }

    fun getProperties(): HttpHandler = {
        val properties: Properties = System.getProperties()
        val allProperties = properties.filter { it.key != null }.map {
            it.key.toString() + ":" + it.value
        }.joinToString("</br>")
        Response(OK).body(allProperties)
    }

    fun getDataSheet(): HttpHandler = {
        val (renderer, _) = buildResourceLoaders(false)
        val viewModel = Wave("M5", 2.1.toLong())
        Response(OK).body(renderer(viewModel))
    }

    private fun buildResourceLoaders(hotReload: Boolean) = when {
        hotReload -> HandlebarsTemplates().HotReload("./src/main/resources") to ResourceLoader.Classpath("public")
        else -> HandlebarsTemplates().CachingClasspath() to ResourceLoader.Classpath("public")
    }
}
