package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.actions.RaceActions1
import com.dhorby.wavemapper.actions.StartRace
import org.http4k.core.Request
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

fun Start(raceActions: RaceActions): (Request) -> WsResponse = {
    StartRace(raceActions).withWsResponse("Success")
}

fun RaceActions1.withWsResponse(message: String): WsResponse {
    return WsResponse { ws: Websocket ->
        ws.send(WsMessage(message))
        ws.close()
    }
}
