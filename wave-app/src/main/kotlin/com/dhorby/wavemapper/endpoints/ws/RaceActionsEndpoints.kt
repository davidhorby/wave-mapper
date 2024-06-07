package com.dhorby.wavemapper.endpoints.ws

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

//    fun addPiece(raceActions: RaceActions): (Request) -> WsResponse = { it ->
//        WsResponse { ws: Websocket ->
//            val name = namePath(req)
//            ws.send(WsMessage("hello $name"))
//            ws.onMessage {
//                ws.send(WsMessage("$name is responding"))
//            }
//            ws.onClose { println("$name is closing") }
//        }
//    }


}




