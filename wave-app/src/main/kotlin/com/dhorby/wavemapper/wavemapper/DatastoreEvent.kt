package com.dhorby.wavemapper.wavemapper

import org.http4k.events.Event

class DatastoreEvent(
    val actor: String,
) : Event {
    operator fun invoke() = println("$actor")
}
