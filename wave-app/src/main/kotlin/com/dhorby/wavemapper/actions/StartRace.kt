package com.dhorby.wavemapper.actions

import com.dhorby.wavemapper.adapter.StorageAdapter
import com.dhorby.wavemapper.game.*


fun resetRace(storageAdapter:StorageAdapter) {
    storageAdapter.write(startLocation)
    storageAdapter.write(finishLocation)
    storageAdapter.write(testSharkLocation)
    storageAdapter.write(testBoatLocation)
    storageAdapter.write(testPirateLocation)
}