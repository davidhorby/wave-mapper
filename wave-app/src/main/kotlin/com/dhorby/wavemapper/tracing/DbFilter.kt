package com.dhorby.wavemapper.tracing

import com.dhorby.gcloud.wavemapper.DatastoreEvent

fun interface DbFilter : (DatastoreEvent) -> Unit {
    companion object
}
