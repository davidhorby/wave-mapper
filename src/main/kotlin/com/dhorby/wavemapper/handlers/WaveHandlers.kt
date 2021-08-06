package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.secrets.AccessSecretVersion
import com.dhorby.wavemapper.html.WavePage
import com.dhorby.wavemapper.XMLWaveParser
import com.dhorby.wavemapper.model.Wave
import org.http4k.core.Body
import org.http4k.core.ContentType.Companion.TEXT_HTML
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates
import org.http4k.template.viewModel
import java.util.*

object WaveHandlers {

//    val renderer = HandlebarsTemplates().HotReload("src/main/resources")

//    val view = Body.viewModel(renderer, TEXT_HTML).toLens()

    fun getWaveHandler(): HttpHandler  = {
        val mapsApiKeyMaybe:String? = AccessSecretVersion.accessSecretVersion("mapsApiKey")
        val metOfficeApiKeyMaybe: String? = AccessSecretVersion.accessSecretVersion("MetOfficeApiKey")

        val displayHtml: String = metOfficeApiKeyMaybe?.let { metOfficeApiKey ->
            mapsApiKeyMaybe?.let { mapsApiKey ->
                val waveData: String = XMLWaveParser.getWaveDataAsGoogleMapFormat(metOfficeApiKey)
                WavePage(waveData, mapsApiKey).waveHtml
            }
        }?: "Missing Met Office API Key"
        Response(OK).body(displayHtml)
    }

    fun getWaveData():HttpHandler = {
        val metOfficeApiKey: String? = AccessSecretVersion.accessSecretVersion("MetOfficeApiKey")
        val waveXML: String = metOfficeApiKey?.let {
            XMLWaveParser.getWaveDataAsJson(it)
        }?: ("Missing Met Office API key")
        Response(OK).body(waveXML)
    }

    fun getProperties():HttpHandler = {
        val properties: Properties = System.getProperties()
        val allProperties = properties.filter { it.key != null }.map {
            it.key.toString() + ":" + it.value
        }.joinToString("</br>")
        Response(OK).body(allProperties)
    }

//    fun getDataSheet(): HttpHandler = {
//        val (renderer, resourceLoader) = buildResourceLoaders(false)
//        val viewModel = Wave("M5", 2.1.toLong())
//        Response(OK).body(renderer(viewModel))
//    }

    private fun buildResourceLoaders(hotReload: Boolean) = when {
        hotReload -> HandlebarsTemplates().HotReload("./src/main/resources") to ResourceLoader.Classpath("public")
        else -> HandlebarsTemplates().CachingClasspath() to ResourceLoader.Classpath("public")
    }
}
