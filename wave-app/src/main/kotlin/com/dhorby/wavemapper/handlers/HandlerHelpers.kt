package com.dhorby.wavemapper.handlers

import com.dhorby.wavemapper.tracing.IncomingHttpRequest
import com.dhorby.wavemapper.tracing.IncomingWsRequest
import com.dhorby.wavemapper.tracing.ReportWsTransaction
import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.events.Event
import org.http4k.filter.ResponseFilters
import org.http4k.routing.RoutingWsHandler
import org.http4k.websocket.then


fun HttpHandler.withEvents(events: (Event) -> Unit) =
    ResponseFilters.ReportHttpTransaction {
        // to "emit" an event, just invoke() the Events!
        events(
            IncomingHttpRequest(
                uri = it.request.uri,
                status = it.response.status.code,
                duration = it.duration.toMillis()
            )
        )
    }.then(this)

fun RoutingWsHandler.withReporting(events: (Event) -> Unit) = ReportWsTransaction {
    events(
        IncomingWsRequest(
            uri = it.request.uri,
            status = 200,
            duration = it.duration.toMillis()
        )
    )
}.then(this)