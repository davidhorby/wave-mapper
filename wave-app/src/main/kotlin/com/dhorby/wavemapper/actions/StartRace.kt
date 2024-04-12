package com.dhorby.wavemapper.actions

import com.dhorby.wavemapper.game.*
import com.dhorby.wavemapper.port.StoragePort


fun resetRace(storagePort: StoragePort) {
    storagePort.write(startLocation)
    storagePort.write(finishLocation)
    storagePort.write(testSharkLocation)
    storagePort.write(testBoatLocation)
    storagePort.write(testPirateLocation)
}