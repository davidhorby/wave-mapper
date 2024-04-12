package com.dhorby.wavemapper.actions

import com.dhorby.gcloud.external.storage.EntityKind
import com.dhorby.gcloud.model.PieceType
import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.dhorby.wavemapper.port.StoragePort

fun startRace(storageAdapter: StorageAdapter) {
    storageAdapter.write(WaveHandlers.start)
    storageAdapter.write(WaveHandlers.finish)
    storageAdapter.getKeysOfType(EntityKind.PIECE_LOCATION, PieceType.BOAT)
        .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
        .forEach(storageAdapter::write)
}

fun clear(storagePort: StoragePort) {
    storagePort.clear(EntityKind.PIECE_LOCATION)
}