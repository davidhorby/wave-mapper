package com.dhorby.wavemapper.routes

import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.endpoints.utils.WsUtils
import com.dhorby.wavemapper.endpoints.ws.RaceActionsEndpoints
import com.dhorby.wavemapper.handlers.withReporting
import com.dhorby.wavemapper.model.WaveResponseWsMessage
import com.dhorby.wavemapper.model.WaveWsMessage
import com.dhorby.wavemapper.model.waveResponseWsMessageLens
import com.dhorby.wavemapper.model.waveWsMessageLens
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.core.Request
import org.http4k.events.Event
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
            "/message" bind {
                WsResponse { ws: Websocket ->
                    println("socket message open")
                    ws.send(WsMessage("""{ "message":"socket message open"}"""))
                    ws.onMessage {
                        val message: WaveResponseWsMessage = waveResponseWsMessageLens(it)
                        println("Got a request" + message)
                        ws.send(WsMessage("""{ "message":"pong ${message.counter}" }"""))
                    }
                }
            },
            "/move" bind (raceActionsEndpoints.Move(raceActions)),
            "/start" bind (raceActionsEndpoints.Start(raceActions)),
            "/clear" bind (raceActionsEndpoints.Clear(raceActions)),
            "/reset" bind (raceActionsEndpoints.Reset(raceActions)),
            "/action" bind { req: Request ->
                WsResponse { ws: Websocket ->
                    println("socket open")
                    ws.send(WsMessage("""{ "message":"socket open"}"""))
                    ws.onMessage {
                        val message: WaveWsMessage = waveWsMessageLens(it)
                        println("Got a request" + message)
                        when(message.action) {
                            "start" -> raceActions.startRace()
                            "clear" -> raceActions.clear()
                            "reset" -> raceActions.resetRace()
                            "move" -> raceActions.move()
                        }
                        val wsResponse = WsUtils.getMapData(storagePort)
                        val returnMessage = WsMessage(wsResponse)
                        val updatedCounter = message.counter?.plus(1) ?: 1
                        ws.send(returnMessage)
                    }
                    ws.onClose {
                        println("socket is closing")
                    }
                }
            }
        )
        return ws.withReporting(events)
    }

    private fun returnMessage(message: String) = WsResponse { ws: Websocket ->
        ws.send(WsMessage(message))
        ws.close()
    }
}