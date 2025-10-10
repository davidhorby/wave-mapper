package com.dhorby.wavemapper.external.google

import org.http4k.chaos.ChaoticHttpHandler
import org.http4k.core.ContentType.Companion.APPLICATION_JSON
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.Header.CONTENT_TYPE
import org.http4k.routing.bind
import org.http4k.routing.routes

class FakeGoogleMapsHandler : ChaoticHttpHandler() {
    val json =
        this::class.java.getResource("/google/mapsdata.json")!!.readText()

    override val app: HttpHandler =
        routes(
            "/maps/api/geocode/json" bind Method.GET to {
                Response(Status.OK)
                    .with(CONTENT_TYPE of APPLICATION_JSON)
                    .body(json)
            },
        )
}
