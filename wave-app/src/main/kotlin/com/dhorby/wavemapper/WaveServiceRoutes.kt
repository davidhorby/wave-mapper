package com.dhorby.wavemapper

import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.handlers.WaveHandlers
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes

object WaveServiceRoutes {

    private val waveServiceFunctions = WaveServiceFunctions()

    private val waveHandlers = WaveHandlers(
        siteListFunction = waveServiceFunctions.siteListFunction,
        dataForSiteFunction = waveServiceFunctions.dataForSiteFunction
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
