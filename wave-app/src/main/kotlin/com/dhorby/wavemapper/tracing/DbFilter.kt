package com.dhorby.wavemapper.tracing

import com.dhorby.wavemapper.wavemapper.DatastoreEvent

fun interface DbFilter : (DatastoreEvent) -> Unit {
    companion object
}
