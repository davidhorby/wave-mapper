package com.dhorby.wavemapper.routes

import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.endpoints.ws.RaceActionsEndpoints
import com.dhorby.wavemapper.handlers.withReporting
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.events.Event
import org.http4k.format.Gson.asJsonObject
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

object WsRoutes {
    operator fun invoke(storagePort: StoragePort, events: (Event) -> Unit, raceActionsEndpoints: RaceActionsEndpoints): RoutingWsHandler {
        val raceActions: RaceActions = RaceActions(storagePort)
        val ws: RoutingWsHandler = websockets(
            "/message/{name}" bind {
                WsResponse { ws: Websocket ->
//                val name = namePath(req)
                    val waveDataOnly = storagePort.getLocationData().toGoogleMapFormatList()
                    returnMessage(waveDataOnly.asJsonObject().toString())
                }
            },
            "/move" bind (raceActionsEndpoints.Move(raceActions)),
            "/start" bind (raceActionsEndpoints.Start(raceActions)),
            "/clear" bind (raceActionsEndpoints.Clear(raceActions)),
            "/reset" bind (raceActionsEndpoints.Reset(raceActions)),
        )
        return ws.withReporting(events)
    }

    private fun returnMessage(message: String) = WsResponse { ws: Websocket ->
        ws.send(WsMessage(message))
        ws.close()
    }
}