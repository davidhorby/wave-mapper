package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.endpoints.utils.WsUtils.generateWsResponse
import com.dhorby.wavemapper.endpoints.utils.WsUtils.getMapData
import com.dhorby.wavemapper.port.StoragePort
import model.PieceLocation
import org.http4k.core.Request
import org.http4k.websocket.WsResponse

class RaceActionsEndpoints(
    private val storagePort: StoragePort,
) {
    fun start(raceActions: RaceActions): (Request) -> WsResponse =
        {
            raceActions.startRace()
            generateWsResponse(getMapData(storagePort))
        }

    fun clear(raceActions: RaceActions): (Request) -> WsResponse =
        {
            raceActions.clear()
            generateWsResponse(getMapData(storagePort))
        }

    fun reset(raceActions: RaceActions): (Request) -> WsResponse =
        {
            raceActions.resetRace()
            generateWsResponse(getMapData(storagePort))
        }

    fun move(raceActions: RaceActions): (Request) -> WsResponse =
        {
            raceActions.move()
            generateWsResponse(getMapData(storagePort))
        }

    fun add(
        raceActions: RaceActions,
        pieceLocation: PieceLocation,
    ) {
        raceActions.addPiece(pieceLocation = pieceLocation)
    }
}
