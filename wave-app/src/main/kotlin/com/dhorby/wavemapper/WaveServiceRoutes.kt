package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.wavemapper.DataStorage
import com.dhorby.gcloud.wavemapper.DatastoreEvent
import com.dhorby.gcloud.wavemapper.WaveServiceFunctions
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.filters.TracingFilter
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.google.cloud.datastore.DatastoreOptions
import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.*
import org.http4k.events.*
import org.http4k.filter.ResponseFilters
import org.http4k.format.Jackson
import org.http4k.lens.Query
import org.http4k.lens.float
import org.http4k.routing.RequestWithRoute
import org.http4k.routing.ResourceLoader.Companion.Classpath
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.routing.static
import org.http4k.server.PolyHandler
import org.http4k.websocket.WsConsumer
import org.http4k.websocket.WsFilter
import org.http4k.websocket.WsResponse
import org.http4k.websocket.then
import java.time.Clock
import java.time.Duration


// here is a new EventFilter that adds custom metadata to the emitted events
fun AddRequestCount(): EventFilter {
    var requestCount = 0
    return EventFilter { next ->
        {
            next(it + ("requestCount" to requestCount++))
        }
    }
}


data class WsTransaction(
    val request: Request,
    val response: WsResponse,
    val duration: Duration,
    val labels: Map<String, String> = when {
        response is WsConsumer -> mapOf(ROUTING_GROUP_LABEL to response.consumer.toString())
        request is RequestWithRoute -> mapOf(ROUTING_GROUP_LABEL to request.xUriTemplate.toString())
        else -> emptyMap()
    }
) {
    fun label(name: String, value: String) = copy(labels = labels + (name to value))
    fun label(name: String) = labels[name]

    companion object {
        internal const val ROUTING_GROUP_LABEL = "routingGroup"
    }
}

data class DbTransaction(
    val request: Request,
    val response: WsResponse,
    val duration: Duration,
    val labels: Map<String, String> = when {
        response is WsConsumer -> mapOf(ROUTING_GROUP_LABEL to response.consumer.toString())
        request is RequestWithRoute -> mapOf(ROUTING_GROUP_LABEL to request.xUriTemplate.toString())
        else -> emptyMap()
    }
) {
    fun label(name: String, value: String) = copy(labels = labels + (name to value))
    fun label(name: String) = labels[name]

    val routingGroup = labels[ROUTING_GROUP_LABEL] ?: "UNMAPPED"

    companion object {
        internal const val ROUTING_GROUP_LABEL = "routingGroup"
    }
}

fun interface DbFilter : (DatastoreEvent) -> Unit {
    companion object
}

//fun DbFilter.then(next: DbFilter): DbFilter = { this(next)(it) }

val isEven = DbFilter { p1 -> p1() }

object ReportDbTransaction {
    operator fun invoke(
        clock: Clock = Clock.systemUTC(),
        transactionLabeler: DbTransactionLabeler = { it },
        recordFn: (DbTransaction) -> Unit
    ): WsFilter = WsFilter { next ->
        {
            clock.instant().let { start ->
                next(it).apply {
                    recordFn(transactionLabeler(DbTransaction(
                        request = it,
                        response = this,
                        duration = Duration.between(start, clock.instant())
                    )))
                }
            }
        }
    }
}

typealias WsTransactionLabeler = (WsTransaction) -> WsTransaction

typealias DbTransactionLabeler = (DbTransaction) -> DbTransaction

object ReportWsTransaction {
    operator fun invoke(
        clock: Clock = Clock.systemUTC(),
        transactionLabeler: WsTransactionLabeler = { it },
        recordFn: (WsTransaction) -> Unit
    ): WsFilter = WsFilter { next ->
        {
            clock.instant().let { start ->
                next(it).apply {
                    recordFn(transactionLabeler(WsTransaction(
                        request = it,
                        response = this,
                        duration = Duration.between(start, clock.instant())
                    )))
                }
            }
        }
    }
}

// this is our custom event which will be printed in a structured way
data class IncomingHttpRequest(val uri: Uri, val status: Int, val duration: Long) : Event

// this is our custom event which will be printed in a structured way
data class IncomingWsRequest(val uri: Uri, val status: Int, val duration: Long) : Event

data class IncomingDbRequest(val uri: Uri, val status: Int, val duration: Long) : Event

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

    private val dataStorage: DataStorage = DataStorage(dataStoreClient)

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

        val handlerWithEvents =
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
