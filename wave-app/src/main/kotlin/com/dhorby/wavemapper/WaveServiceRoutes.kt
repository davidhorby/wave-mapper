package com.dhorby.wavemapper

import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.handlers.WaveHandlers
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.format.Jackson
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

object WaveServiceRoutes {

    private val waveServiceFunctions = WaveServiceFunctions()

    private val waveHandlers = WaveHandlers(
        siteListFunction = waveServiceFunctions.siteListFunction,
        dataForSiteFunction = waveServiceFunctions.dataForSiteFunction
    )

    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    operator fun invoke(): HttpHandler =
        routes(

            "/ping" bind Method.GET to {
                Response(Status.OK).body("pong")
            },

            "/" bind Method.GET to waveHandlers.getWavePage(),
            "/data" bind Method.GET to waveHandlers.getWaveData(),
            "/properties" bind Method.GET to waveHandlers.getProperties(),
            "/datasheet" bind Method.GET to waveHandlers.getDataSheet(),
            "/map" bind Method.GET to waveHandlers.getMap(),
            "/css" bind static(Classpath("/css")),
            "/api" bind contract {
                renderer = OpenApi3(ApiInfo("Wave Mapper API", "v1.0"), Jackson)
                routes += "/location"  meta {
                    summary = "Get location data from Google API for co-ordinates"
                    queries += latQuery
                    queries += lonQuery
                } bindContract Method.GET to waveHandlers.getLocationData()
            },static(Classpath("public"))
        )

}
