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
import com.dhorby.wavemapper.port.StoragePort
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
        val storagePort: StoragePort = StorageAdapter(dataStoreClient)
        resetRace(storagePort = storagePort)
        val finishAndStartLocations: List<PieceLocation> = storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START)
        assertThat(finishAndStartLocations,
            hasElement(startLocation)
                    and hasElement(finishLocation))
    }

    @Test
    fun `should be able to remove all pieces`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val storagePort: StoragePort = StorageAdapter(dataStoreClient)
        resetRace(storagePort = storagePort)
        clear(storagePort = storagePort)
        val finishAndStartLocations: List<PieceLocation> = storagePort.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.START)
        assertThat(finishAndStartLocations, isEmpty)
    }

    @Test
    fun `datatstore events should be traced`(datastore: Datastore) {
        val dataStoreClient = DataStoreClient(events = events, datastore = datastore)
        val storagePort: StoragePort = StorageAdapter(dataStoreClient)
        resetRace(storagePort = storagePort)
        assertThat(eventLog, hasSize(greaterThan(0)))
        assertThat(eventLog[0], containsSubstring("writing to datastore"))
    }
}