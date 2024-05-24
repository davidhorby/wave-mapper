package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.wavemapper.actions.RaceActions
import org.http4k.core.Request
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

object RaceActionsEndpoints {
    fun Start(raceActions: RaceActions): (Request) -> WsResponse = { generateWsResponse(raceActions.startRace()) }

    fun Clear(raceActions: RaceActions): (Request) -> WsResponse = { generateWsResponse(raceActions.clear()) }

    fun Reset(raceActions: RaceActions): (Request) -> WsResponse = { generateWsResponse(raceActions.resetRace()) }

    fun Move(raceActions: RaceActions): (Request) -> WsResponse = { generateWsResponse(raceActions.move()) }

    private fun generateWsResponse(message: String): WsResponse {
        return WsResponse { ws: Websocket ->
            ws.send(WsMessage(message))
            ws.close()
        }
    }
}




