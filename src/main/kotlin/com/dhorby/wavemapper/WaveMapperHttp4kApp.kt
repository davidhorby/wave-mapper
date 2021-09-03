package com.dhorby.wavemapper

import com.dhorby.wavemapper.handlers.WaveHandlers
import org.http4k.core.*
import org.http4k.filter.DebuggingFilters
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.SunHttp
import org.http4k.server.asServer


val app: HttpHandler = routes(
    "/ping" bind Method.GET to {
        Response(Status.OK).body("pong")
    },

    "/" bind Method.GET to WaveHandlers.getWavePage(),
    "/data" bind Method.GET to WaveHandlers.getWaveData(),
    "/properties" bind Method.GET to WaveHandlers.getProperties(),
    "/datasheet" bind Method.GET to WaveHandlers.getDataSheet()
)

fun main() {
    val printingApp: HttpHandler = DebuggingFilters.PrintRequest().then(app)

    val server = printingApp.asServer(SunHttp(8080)).start()

    println("Server started on http://localhost:" + server.port())
}


