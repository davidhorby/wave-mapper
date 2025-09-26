package com.dhorby.wavemapper.tracing

import org.http4k.core.Uri
import org.http4k.events.Event

// fun DbFilter.then(next: DbFilter): DbFilter = { this(next)(it) }

// this is our custom event which will be printed in a structured way
data class IncomingHttpRequest(
    val uri: Uri,
    val status: Int,
    val duration: Long,
) : Event

// this is our custom event which will be printed in a structured way
data class IncomingWsRequest(
    val uri: Uri,
    val status: Int,
    val duration: Long,
) : Event

data class IncomingDbRequest(
    val uri: Uri,
    val status: Int,
    val duration: Long,
) : Event
