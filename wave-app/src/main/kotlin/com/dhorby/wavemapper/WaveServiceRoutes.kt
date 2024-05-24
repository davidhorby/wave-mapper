package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.DataStoreClient
import com.dhorby.wavemapper.adapter.StorageAdapter
import org.http4k.events.Event
import org.http4k.server.PolyHandler


object WaveServiceRoutes {
    operator fun invoke(dataStoreClient: DataStoreClient, events: (Event) -> Unit): PolyHandler = PolyHandler(
        HttpRoutes(dataStoreClient, events), WsRoutes(storagePort = StorageAdapter(dataStoreClient), events)
    )
}
