package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.wavemapper.routes.WaveServiceRoutes
import com.google.cloud.datastore.DatastoreOptions
import org.http4k.events.*
import org.http4k.format.Jackson
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// here is a new EventFilter that adds custom metadata to the emitted events
fun AddRequestCount(): EventFilter {
    var requestCount = 0
    return EventFilter { next ->
        {
            next(it + ("requestCount" to requestCount++))
        }
    }
}

object WaveMapperHttp4kApp {

    private val LOG: Logger = LoggerFactory.getLogger(WaveMapperHttp4kApp::class.java)

    @JvmStatic
    fun main(args: Array<String>) {

        val events: (Event) -> Unit =
            EventFilters.AddTimestamp()
                .then(EventFilters.AddEventName())
                .then(EventFilters.AddZipkinTraces())
                .then(AddRequestCount())
                .then(AutoMarshallingEvents(Jackson))

        LOG.info("Starting the Wave Mapper App")
        val datastoreClient = DataStoreClient(events, DatastoreOptions.getDefaultInstance().service!!)
        val server = WaveServiceRoutes(datastoreClient, events).asServer(Jetty(8080)).start()
        println("Server started on " + server.port())
    }
}


