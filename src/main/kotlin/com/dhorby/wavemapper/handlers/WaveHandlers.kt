package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.XMLWaveParser
import com.dhorby.wavemapper.XMLWaveParser.getAllWaveData
import com.dhorby.wavemapper.datautils.toGoogleMapFormat
import com.dhorby.wavemapper.model.Wave
import com.dhorby.wavemapper.model.WavePage
import com.dhorby.wavemapper.secrets.AccessSecretVersion
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.ViewModel
import java.util.*

object WaveHandlers {

    private const val devMode = false;

    //    val renderer = HandlebarsTemplates().HotReload("src/main/resources")
    val renderer = when {
        devMode -> HandlebarsTemplates().HotReload("src/main/resources")
        else -> HandlebarsTemplates().CachingClasspath()
    }

    fun getWavePage(): HttpHandler = {
        val mapsApiKeyMaybe: String? = AccessSecretVersion.accessSecretVersion("mapsApiKey")
        val metOfficeApiKeyMaybe: String? = AccessSecretVersion.accessSecretVersion("MetOfficeApiKey")


        val viewModel: ViewModel? = metOfficeApiKeyMaybe?.let { metOfficeApiKey ->
            mapsApiKeyMaybe?.let { mapsApiKey ->
                val waveData: String = getAllWaveData(metOfficeApiKey).toGoogleMapFormat()
                WavePage(waveData, mapsApiKey)
            }
        }


        viewModel?.let {
            try {
                Response(OK).body(renderer(viewModel))
            } catch (e: Exception) {
                Response(OK).body(e.stackTraceToString())
            }
        } ?: Response(OK).body("Missing Met Office API Key")

    }


    fun getWaveData(): HttpHandler = {
        val metOfficeApiKey: String? = AccessSecretVersion.accessSecretVersion("MetOfficeApiKey")
        val waveXML: String = metOfficeApiKey?.let {
            XMLWaveParser.getWaveDataAsJson(it)
        } ?: ("Missing Met Office API key")
        Response(OK).body(waveXML)
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
