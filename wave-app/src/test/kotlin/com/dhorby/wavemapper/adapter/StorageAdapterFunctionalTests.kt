package com.dhorby.wavemapper.adapter

import com.dhorby.wavemapper.model.GeoLocation
import com.dhorby.wavemapper.model.PieceLocation
import com.dhorby.wavemapper.model.PieceType
import com.dhorby.wavemapper.tracing.addRequestCount
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Value
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson

class StorageAdapterFunctionalTests : StorageAdapterContract {
    private val events: (Event) -> Unit =
        EventFilters
            .AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(addRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    override val pieceLocation =
        PieceLocation(
            id = "test",
            name = "test piece",
            geoLocation = GeoLocation(-0.34, 45.03),
            pieceType = PieceType.BOAT,
        )
    override val multiplePieces: List<PieceLocation> =
        listOf(
            pieceLocation.copy(
                id = "mp-test1",
                name = "first piece",
                geoLocation = GeoLocation(34.12, 32.42),
                pieceType = PieceType.PIRATE,
            ),
            pieceLocation.copy(
                id = "mp-test2",
                name = "second piece",
                geoLocation = GeoLocation(23.32, 67.87),
                pieceType = PieceType.PIRATE,
            ),
        )
    override val entityMatcher: Matcher<Entity> =
        isA<Entity>(
            has("reputation", { it.getValue<Value<String>>("name").get() }, equalTo("test piece")),
        )
    override val pieceLocationMatcher: Matcher<PieceLocation> = isA<PieceLocation>(equalTo(pieceLocation))
}
