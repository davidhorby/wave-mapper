package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.handlers.withEvents
import com.dhorby.wavemapper.handlers.withReporting
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.events.Event
import org.http4k.format.Jackson
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.PolyHandler


object WaveServiceRoutes {

    private val waveServiceFunctions = WaveServiceFunctions()

    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    operator fun invoke(dataStoreClient: DataStoreClient, events: (Event) -> Unit): PolyHandler {

        val waveHandlers = WaveHandlers(
            siteListFunction = waveServiceFunctions.siteListFunction,
            dataForSiteFunction = waveServiceFunctions.dataForSiteFunction,
            storageAdapter = StorageAdapter(dataStoreClient)
        )

        val httpHandler: HttpHandler = routes(
            "/ping" bind Method.GET to {
                Response(Status.OK).body("pong")
            },
            "/" bind Method.GET to waveHandlers.getWavePage(),
            "/data" bind Method.GET to waveHandlers.getWaveData(),
            "/properties" bind Method.GET to waveHandlers.getProperties(),
            "/datasheet" bind Method.GET to waveHandlers.getDataSheet(),
            "/map" bind Method.GET to waveHandlers.getMap(),
            "/" bind Method.POST to waveHandlers.addPiece(),
            "/start" bind Method.GET to waveHandlers.start(),
            "/move" bind Method.GET to waveHandlers.move(),
            "/css" bind static(
                Classpath("/css")
            ),
            "/api" bind contract {
                renderer = OpenApi3(ApiInfo("Wave Mapper API", "v1.0"), Jackson)
                routes += "/location" meta {
                    summary = "Get location data from Google API for co-ordinates"
                    queries += latQuery
                    queries += lonQuery
                } bindContract Method.GET to waveHandlers.getLocationData()
            }, static(Classpath("public"))
        ).withEvents(events)

        val webSocketHandler: RoutingWsHandler = WebSocketRoutes(
            storagePort = StorageAdapter(dataStoreClient)
        ).withReporting(events)

        return PolyHandler(
            httpHandler, webSocketHandler
        )
    }
}
