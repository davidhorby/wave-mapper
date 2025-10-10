package com.dhorby.wavemapper.tracing

import org.http4k.events.EventFilter
import org.http4k.events.plus

// here is a new EventFilter that adds custom metadata to the emitted events
fun addRequestCount(): EventFilter {
    var requestCount = 0
    return EventFilter { next ->
        {
            next(it + ("requestCount" to requestCount++))
        }
    }
}
