package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.secrets.AccessSecretVersion
import com.dhorby.wavemapper.html.WavePage
import com.dhorby.wavemapper.XMLWaveParser
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import java.util.*

object WaveHandlers {

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
}
