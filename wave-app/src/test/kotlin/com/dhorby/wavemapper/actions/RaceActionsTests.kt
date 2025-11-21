package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.game.testBoatLocation
import com.dhorby.wavemapper.storage.DataStoreClient
import com.dhorby.wavemapper.storage.EntityKind
import com.dhorby.wavemapper.tracing.addRequestCount
import com.google.cloud.datastore.Datastore
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
class RaceActionsTests {
    private val events: (Event) -> Unit =
        EventFilters
            .AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(addRequestCount())
            .then(AutoMarshallingEvents(Jackson))

    @Test
    fun `should store a game piece`(datastore: Datastore) {
        val (storagePort, raceActions: RaceActions) = getStoragePort(datastore)
        val pieceLocation = testBoatLocation
        println(datastore)
        raceActions.addPiece(pieceLocation)
        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
    }

//    @Test
//    fun `should delete a game piece`(datastore: Datastore) {
//        val (storagePort, raceActions: RaceActions) = getStoragePort(datastore)
//        val pieceLocation = testBoatLocation
//        raceActions.addPiece(pieceLocation)
//        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), equalTo(testBoatLocation))
//        raceActions.deletePiece(pieceLocation)
//        assertThat(storagePort.getPiece(EntityKind.PIECE_LOCATION, key = pieceLocation.id), absent())
//    }
//
//    @Test
//    fun `should not start the race of there are no players`(datastore: Datastore) {
//        val (storagePort, raceActions: RaceActions) = getStoragePort(datastore)
//        val startResponse = raceActions.startRace()
//        assertThat(startResponse, equalTo("Not enough players"))
//    }
//
    private fun getStoragePort(datastore: Datastore): Pair<StorageAdapter, RaceActions> {
        val dataStoreClient = DataStoreClient(datastore = datastore)
        val storagePort = StorageAdapter(dataStoreClient)
        val raceActions: RaceActions = RaceActions(storagePort)
        return Pair(storagePort, raceActions)
    }
}
