package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.asJson
import com.dhorby.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.getAllWaveData
import com.dhorby.wavemapper.model.SiteLocation
import com.dhorby.wavemapper.model.Wave
import com.dhorby.wavemapper.model.WavePage
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.util.*

class WaveHandlers(
    val siteListFunction: () -> List<SiteLocation>,
    val metOfficeApiKey: String,
    val mapsApiKey: String
) {

    private val devMode = false;

    //    val renderer = HandlebarsTemplates().HotReload("src/main/resources")
    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }


    fun getWavePage(): HttpHandler = {

        val viewModel: ViewModel = metOfficeApiKey.let { metOfficeApiKey ->
            mapsApiKey.let { mapsApiKey ->
                val waveData: String =
                    getAllWaveData(siteList = siteListFunction).toGoogleMapFormat()
                WavePage(waveData, mapsApiKey)
            }
        }


        viewModel.let {
            try {
                Response(OK).body(renderer(viewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        } ?: Response(OK).body("Missing Met Office API Key")

    }


    fun getWaveData(metOfficeApiKey:String): HttpHandler = {
        Response(OK).body(getAllWaveData(siteListFunction).asJson())
    }

    fun getProperties(): HttpHandler = {
        val properties: Properties = System.getProperties()
        val allProperties = properties.filter { it.key != null }.map {
            it.key.toString() + ":" + it.value
        }.joinToString("</br>")
        Response(OK).body(allProperties)
    }

    fun getDataSheet(): HttpHandler = {
        val (renderer, resourceLoader) = buildResourceLoaders(false)
        val viewModel = Wave("M5", 2.1.toLong())
        Response(OK).body(renderer(viewModel))
    }

    private fun buildResourceLoaders(hotReload: Boolean) = when {
        hotReload -> HandlebarsTemplates().HotReload("./src/main/resources") to ResourceLoader.Classpath("public")
        else -> HandlebarsTemplates().CachingClasspath() to ResourceLoader.Classpath("public")
    }
}
