package com.dhorby.wavemapper

import com.dhorby.wavemapper.Constants.Companion.mapsApiKey
import com.dhorby.wavemapper.Constants.Companion.metOfficeApiKey
import com.dhorby.wavemapper.WaveServiceFunctions.dataForSiteFunction
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer
import java.net.URL

object WaveServiceFunctions {
    val dataForSiteFunction:DataForSiteFunction = { site->
        try {
            val metOfficeUrl = getMetOfficeUrl(site)
            val xmlText = URL(metOfficeUrl).readText()
            val xmlMapper = XmlMapper()
            val jsonNode: JsonNode = xmlMapper.readTree(xmlText)
            jsonNode.getLocation()
        } catch (ex: Exception) {
            println("Failed to read url ${URL(Constants.metOfficeUrl)} ${ex.message}")
            null
        }
    }
}


object WaveServiceRoutes {



    private val waveHandlers = WaveHandlers(
        siteListFunction = siteListFunction,
        dataForSiteFunction = dataForSiteFunction
    )

    operator fun invoke(): HttpHandler =
        routes(

            "/ping" bind Method.GET to {
                Response(Status.OK).body("pong")
            },

            "/" bind Method.GET to waveHandlers.getWavePage(),
            "/data" bind Method.GET to waveHandlers.getWaveData(),
            "/properties" bind Method.GET to waveHandlers.getProperties(),
            "/datasheet" bind Method.GET to waveHandlers.getDataSheet()
        )

}

fun main() {
    val printingApp: HttpHandler = DebuggingFilters.PrintRequest().then(WaveServiceRoutes())

    val server = printingApp.asServer(SunHttp(8080)).start()

    println("Server started on http://localhost:" + server.port())
}


