package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.game.finishLocation
import com.dhorby.wavemapper.game.startLocation
import com.dhorby.wavemapper.game.testBoatLocation
import com.dhorby.wavemapper.port.StoragePort
import com.dhorby.wavemapper.tracing.addRequestCount
import com.google.cloud.datastore.Datastore
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import com.natpryce.hamkrest.greaterThan
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.hasSize
import com.natpryce.hamkrest.isEmpty
import model.PieceLocation
import org.http4k.events.AutoMarshallingEvents
import org.http4k.events.Event
import org.http4k.events.EventFilters
import org.http4k.events.and
import org.http4k.events.then
import org.http4k.format.Gson.asJsonObject
import org.http4k.format.Jackson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
internal class StartRaceKtTest {
    private val eventLog = mutableListOf<String>()

    private val events: (Event) -> Unit =
        EventFilters
            .AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(addRequestCount())
            .then(AutoMarshallingEvents(Jackson))
            .and {
                eventLog.add(it.asJsonObject().toString())
            }

    @Test
    fun `should be able to add a piece`(datastore: Datastore) {
        val raceActions = raceActions(datastore)
        raceActions.addPiece(testBoatLocation)
    }

    @Test
    fun `should be able to reset race back to starting positions`(datastore: Datastore) {
        val raceActions = raceActions(datastore)
        raceActions.resetRace()
        val finishAndStartLocations: List<PieceLocation> = raceActions.getStartAndFinish()
        assertThat(
            finishAndStartLocations,
            hasElement(startLocation)
                and hasElement(finishLocation),
        )
    }

    @Test
    fun `should be able to remove all pieces`(datastore: Datastore) {
        val raceActions = raceActions(datastore)
        raceActions.resetRace()
        raceActions.clear()
        val finishAndStartLocations: List<PieceLocation> = raceActions.getStartAndFinish()
        assertThat(finishAndStartLocations, isEmpty)
    }

    @Test
    fun `storage events should be traced`(datastore: Datastore) {
        val raceActions = raceActions(datastore)
        raceActions.resetRace()
        assertThat(eventLog, hasSize(greaterThan(0)))
        assertThat(eventLog[0], containsSubstring("writing to datastore"))
    }

    private fun raceActions(datastore: Datastore): RaceActions {
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val storagePort: StoragePort = StorageAdapter(dataStoreClient)
        val raceActions = RaceActions(storagePort)
        return raceActions
    }
}
