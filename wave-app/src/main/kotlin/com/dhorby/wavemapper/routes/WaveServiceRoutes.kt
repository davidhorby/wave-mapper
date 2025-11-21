package com.dhorby.wavemapper.routes

import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.endpoints.ws.RaceActionsEndpoints
import com.dhorby.wavemapper.storage.DataStoreClient
import org.http4k.events.Event
import org.http4k.server.PolyHandler

object WaveServiceRoutes {
    operator fun invoke(
        dataStoreClient: DataStoreClient,
        events: (Event) -> Unit,
    ): PolyHandler {
        val raceActionsEndpoints = RaceActionsEndpoints(StorageAdapter(dataStoreClient))
        return PolyHandler(
            HttpRoutes(dataStoreClient, events),
            WsRoutes(
                storagePort = StorageAdapter(dataStoreClient),
                events = events,
                raceActionsEndpoints = raceActionsEndpoints,
            ),
        )
    }
}
