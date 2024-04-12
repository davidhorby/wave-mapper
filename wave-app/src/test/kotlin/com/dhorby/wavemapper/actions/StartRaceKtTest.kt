package com.dhorby.wavemapper.actions

import AddRequestCount
import com.dhorby.gcloud.external.junit.DataStoreExtension
import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.game.finishLocation
import com.dhorby.wavemapper.game.startLocation
import com.google.cloud.datastore.Datastore
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import org.http4k.events.*
import org.http4k.format.Gson.asJsonObject
import org.http4k.format.Jackson
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(DataStoreExtension::class)
internal class StartRaceKtTest {

    private val eventLog = mutableListOf<String>()

    private val events: (Event) -> Unit =
        EventFilters.AddTimestamp()
            .then(EventFilters.AddEventName())
            .then(EventFilters.AddZipkinTraces())
            .then(AddRequestCount())
            .then(AutoMarshallingEvents(Jackson))
            .and {
                eventLog.add(it.asJsonObject().toString())
            }

    @Test
    fun `should be able to reset race back to starting positions`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        resetRace(storageAdapter = storageAdapter)
        val finishAndStartLocations: List<PieceLocation> = storageAdapter.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START)
        assertThat(finishAndStartLocations,
            hasElement(startLocation)
                and hasElement(finishLocation))
    }

    @Test
    fun `datatstore events should be traced`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val storageAdapter = StorageAdapter(dataStoreClient)
        resetRace(storageAdapter = storageAdapter)
        val finishAndStartLocations: List<PieceLocation> = storageAdapter.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START)
        assertThat(eventLog, hasSize(greaterThan(0)))
        assertThat(eventLog[0], containsSubstring("writing to datastore"))
    }
}