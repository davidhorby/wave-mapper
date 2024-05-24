package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.wavemapper.AddRequestCount
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.game.testBoatLocation
import com.google.cloud.datastore.Datastore
import com.natpryce.hamkrest.absent
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.then
import org.http4k.format.Jackson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
class RaceActionsContract {

    val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    @Test
    fun addPiece(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events, datastore = datastore)
        val storagePort = StorageAdapter(dataStoreClient)
        val raceActions: RaceActions = RaceActions(storagePort)
        val pieceLocation = testBoatLocation
        raceActions.addPiece(pieceLocation)
        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
    }

    @Test
    fun deletePiece(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events, datastore = datastore)
        val storagePort = StorageAdapter(dataStoreClient)
        val raceActions: RaceActions = RaceActions(storagePort)
        val pieceLocation = testBoatLocation
        raceActions.addPiece(pieceLocation)
        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
        raceActions.deletePiece(pieceLocation)
        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), absent())
    }
}