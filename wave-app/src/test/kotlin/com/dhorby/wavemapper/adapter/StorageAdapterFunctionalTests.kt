package com.dhorby.wavemapper.adapter

import AddRequestCount
import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.external.storage.Storable
import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Value
import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import com.natpryce.hamkrest.isA
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.AutoMarshallingEvents.invoke
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.junit.jupiter.api.extension.RegisterExtension

class StorageAdapterFunctionalTests:StorageAdapterContract {

    companion object {
        @JvmStatic
        @RegisterExtension
        val server = DataStoreExtension()
            .builder()
            .build()
    }

    private val datastore = server.localDatastoreHelper.options.service
    private val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    override val dataStoreClient: Storable = DataStoreClient(datastore = datastore, events = events)
    override val pieceLocation = PieceLocation(
        id = "test",
        name = "test piece",
        geoLocation = GeoLocation(-0.34, 45.03),
        pieceType = PieceType.BOAT
    )
    override val multiplePieces: List<PieceLocation> = listOf(pieceLocation, pieceLocation.copy(
        id="test2",
        name="second piece",
        geoLocation = GeoLocation(23.32, 67.87),
        pieceType = PieceType.PIRATE
    ))
    override val entityMatcher: Matcher<Entity>  = isA<Entity>(
        has("reputation", { it.getValue<Value<String>>("name").get() }, equalTo("test piece"))
    )
    override val pieceLocationMatcher: Matcher<PieceLocation> = isA<PieceLocation>(equalTo(pieceLocation))

//    init {
//        dataStoreClient.writeToDatastore(PIECE_LOCATION_KIND, pieceLocation)
//    }
}