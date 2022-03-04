package com.dhorby.wavemapper

import com.dhorby.wavemapper.Constants.Companion.mapsApiKey
import com.dhorby.wavemapper.Constants.Companion.metOfficeApiKey
import com.dhorby.wavemapper.handlers.WaveHandlers
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer


object WaveServiceRoutes {


    private val waveHandlers = WaveHandlers(
        siteListFunction = siteListFunction,
        metOfficeApiKey = metOfficeApiKey,
        mapsApiKey = mapsApiKey
    )

    operator fun invoke(): HttpHandler =
        routes(

            "/ping" bind Method.GET to {
                Response(Status.OK).body("pong")
            },

            "/" bind Method.GET to waveHandlers.getWavePage(),
            "/data" bind Method.GET to waveHandlers.getWaveData(metOfficeApiKey),
            "/properties" bind Method.GET to waveHandlers.getProperties(),
            "/datasheet" bind Method.GET to waveHandlers.getDataSheet()
        )

}

fun main() {
    val printingApp: HttpHandler = DebuggingFilters.PrintRequest().then(WaveServiceRoutes())

    val server = printingApp.asServer(SunHttp(8080)).start()

    println("Server started on http://localhost:" + server.port())
}


