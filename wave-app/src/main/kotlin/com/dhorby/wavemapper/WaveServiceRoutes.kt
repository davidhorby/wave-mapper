package com.dhorby.wavemapper

import AddRequestCount
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.filters.TracingFilter
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.tracing.IncomingHttpRequest
import com.dhorby.wavemapper.tracing.IncomingWsRequest
import com.dhorby.wavemapper.tracing.ReportWsTransaction
import com.google.cloud.datastore.DatastoreOptions
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.*
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.filter.ResponseFilters
import org.http4k.format.Jackson
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.PolyHandler
import org.http4k.websocket.then


object WaveServiceRoutes {

    val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    private val tracingFilter = TracingFilter()

    private val waveServiceFunctions = WaveServiceFunctions()

    private val dataStoreClient = DataStoreClient(events, DatastoreOptions.getDefaultInstance().service!!)

//    private val dbHandler = ReportDbTransaction.invoke {
//        events(
//            IncomingDbRequest(
//                uri = it.request.uri,
//                status = 200,
//                duration = it.duration.toMillis()
//            )
//        )
//    }.then(TODO())


    private val waveHandlers: WaveHandlers = WaveHandlers(
        siteListFunction = waveServiceFunctions.siteListFunction,
        dataForSiteFunction = waveServiceFunctions.dataForSiteFunction,
        storageAdapter = StorageAdapter(dataStoreClient)
    )

    val latQuery = Query.float().required("lat")
    val lonQuery = Query.float().required("lon")

    operator fun invoke(): PolyHandler {

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
        )

        val handlerWithEvents: HttpHandler =
            ResponseFilters.ReportHttpTransaction {
                // to "emit" an event, just invoke() the Events!
                events(
                    IncomingHttpRequest(
                        uri = it.request.uri,
                        status = it.response.status.code,
                        duration = it.duration.toMillis()
                    )
                )
            }.then(httpHandler)

        val webSocketRoutes = WebSocketRoutes(
            storageAdapter =  StorageAdapter(dataStoreClient)
        )

        val reportWsTransaction = ReportWsTransaction {
            events(
                IncomingWsRequest(
                    uri = it.request.uri,
                    status = 200,
                    duration = it.duration.toMillis()
                )
            )
        }.then(webSocketRoutes.ws)

        return PolyHandler(
            handlerWithEvents, reportWsTransaction
        )
    }
}
