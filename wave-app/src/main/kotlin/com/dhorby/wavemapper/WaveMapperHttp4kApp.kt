package com.dhorby.wavemapper

import com.dhorby.wavemapper.routes.WaveServiceRoutes
import com.dhorby.wavemapper.storage.DataStoreClient
import com.dhorby.wavemapper.tracing.addRequestCount
import com.google.cloud.datastore.DatastoreOptions
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object WaveMapperHttp4kApp {
    private val LOG: Logger = LoggerFactory.getLogger(WaveMapperHttp4kApp::class.java)

    @JvmStatic
    fun main(args: Array<String>) {
        val events: (Event) -> Unit =
            EventFilters
                .AddTimestamp()
                .then(EventFilters.AddEventName())
                .then(EventFilters.AddZipkinTraces())
                .then(addRequestCount())
                .then(AutoMarshallingEvents(Jackson))

        LOG.info("Starting the Wave Mapper App")
        val datastoreClient = DataStoreClient(DatastoreOptions.getDefaultInstance().service!!)
        val server = WaveServiceRoutes(datastoreClient, events).asServer(Jetty(8080)).start()
        println("Server started on " + server.port())
    }
}
