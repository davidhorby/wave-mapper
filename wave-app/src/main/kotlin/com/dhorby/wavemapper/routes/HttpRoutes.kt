package com.dhorby.wavemapper.routes

import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.adapter.WaveAdapter
import com.dhorby.wavemapper.adapter.WaveDataAdapter
import com.dhorby.wavemapper.endpoints.http.DataSheet
import com.dhorby.wavemapper.endpoints.http.Properties
import com.dhorby.wavemapper.endpoints.http.WaveData
import com.dhorby.wavemapper.endpoints.http.WaveMap
import com.dhorby.wavemapper.external.google.GoogleMapsClientApi
import com.dhorby.wavemapper.external.metoffice.MetOfficeClient
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.handlers.withEvents
import com.dhorby.wavemapper.port.WaveDataPort
import org.http4k.client.ApacheClient
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
import org.http4k.routing.ResourceLoader
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static

object HttpRoutes {
    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    operator fun invoke(
        dataStoreClient: DataStoreClient,
        events: (Event) -> Unit,
    ): HttpHandler {
        val apacheHandler: HttpHandler = ApacheClient()
        val siteListFunction = WaveServiceFunctions.siteListFunction
        val dataForSiteFunction = WaveServiceFunctions.dataForSiteFunction
        val storageAdapter = StorageAdapter(dataStoreClient)
        val waveDataAdapter: WaveDataPort = WaveDataAdapter(siteListFunction, dataForSiteFunction)
        val waveHandlers =
            WaveHandlers(
                wavePort =
                    WaveAdapter(
                        storageAdapter = storageAdapter,
                        googleMapsClientApi = GoogleMapsClientApi(apacheHandler),
                        metOfficeClient = MetOfficeClient(),
                    ),
            )

        val httpHandler: HttpHandler =
            routes(
                "/ping" bind Method.GET to {
                    Response(Status.OK).body("pong")
                },
                "/" bind Method.GET to waveHandlers.getWavePage(),
                "/data" bind Method.GET to WaveData(waveDataAdapter),
                "/properties" bind Method.GET to Properties(),
                "/datasheet" bind Method.GET to DataSheet(),
                "/map" bind Method.GET to WaveMap(),
                "/" bind Method.POST to waveHandlers.addPiece(),
                "/css" bind
                    static(
                        ResourceLoader.Classpath("/css"),
                    ),
                "/api" bind
                    contract {
                        renderer = OpenApi3(ApiInfo("Wave Mapper API", "v1.0"), Jackson)
                        routes += "/location" meta {
                            summary = "Get location data from Google API for co-ordinates"
                            queries += latQuery
                            queries += lonQuery
                        } bindContract Method.GET to waveHandlers.getLocationData()
                    },
                static(ResourceLoader.Classpath("public")),
            ).withEvents(events)
        return httpHandler
    }
}
