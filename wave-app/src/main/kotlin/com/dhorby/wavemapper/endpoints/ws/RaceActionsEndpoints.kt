package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.gcloud.model.PieceLocation
import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.endpoints.utils.WsUtils.generateWsResponse
import com.dhorby.wavemapper.endpoints.utils.WsUtils.getMapData
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.core.Request
import org.http4k.websocket.WsResponse

class RaceActionsEndpoints(private val storagePort: StoragePort) {
    fun Start(raceActions: RaceActions): (Request) -> WsResponse = {
        raceActions.startRace()
        generateWsResponse(getMapData(storagePort))
    }

    fun Clear(raceActions: RaceActions): (Request) -> WsResponse = {
        raceActions.clear()
        generateWsResponse(getMapData(storagePort))
    }

    fun Reset(raceActions: RaceActions): (Request) -> WsResponse = {
        raceActions.resetRace()
        generateWsResponse(getMapData(storagePort))
    }

    fun Move(raceActions: RaceActions): (Request) -> WsResponse = {
        raceActions.move()
        generateWsResponse(getMapData(storagePort))
    }

    fun Add(raceActions: RaceActions, pieceLocation: PieceLocation) {
        raceActions.addPiece(pieceLocation = pieceLocation)
    }

}




