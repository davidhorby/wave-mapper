package com.dhorby.wavemapper.routes

import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.addPieceWsMessageLens
import com.dhorby.wavemapper.endpoints.utils.WsUtils
import com.dhorby.wavemapper.endpoints.ws.RaceActionsEndpoints
import com.dhorby.wavemapper.handlers.withReporting
import com.dhorby.wavemapper.model.WaveWsMessage
import com.dhorby.wavemapper.model.toPieceLocation
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
    var wsMessageSocket: Websocket? = null
    var wsActionSocket: Websocket? = null
    var wsPostSocket: Websocket? = null

    operator fun invoke(
        storagePort: StoragePort,
        events: (Event) -> Unit,
        raceActionsEndpoints: RaceActionsEndpoints
    ): RoutingWsHandler {
        val raceActions: RaceActions = RaceActions(storagePort)

        val ws: RoutingWsHandler = websockets(
            "/message" bind {
                WsResponse { messageSocketLocal ->
                    wsMessageSocket = messageSocketLocal
                    println("socket message open")
                    sendMessage("socket message open")
                    messageSocketLocal.onMessage { message ->
                        handleAddPiece(message, raceActions, storagePort)
                    }
                    messageSocketLocal.onClose {
                        println("message socket is closing")
                    }
                }
            },
            "/move" bind (raceActionsEndpoints.Move(raceActions)),
            "/start" bind (raceActionsEndpoints.Start(raceActions)),
            "/clear" bind (raceActionsEndpoints.Clear(raceActions)),
            "/reset" bind (raceActionsEndpoints.Reset(raceActions)),
            "/post" bind { req: Request ->
                WsResponse { postSocketLocal ->
                    wsPostSocket = postSocketLocal
                    println("post socket open")
                    postSocketLocal.onMessage { message: WsMessage ->
                        val pieceLocation = addPieceWsMessageLens(message).toPieceLocation()
                        raceActionsEndpoints.Add(raceActions, pieceLocation)
                        returnMapData(storagePort, "add piece")
                    }
                    returnMapData(storagePort, "add piece")
                    postSocketLocal.onClose {
                        println("post socket is closing")
                    }
                }
            },

            "/action" bind { req: Request ->
                WsResponse { actionSocketLocal ->
                    wsActionSocket = actionSocketLocal
                    println("socket open")
                    actionSocketLocal.send(WsMessage("""{ "message":"socket open"}"""))
                    actionSocketLocal.onMessage {
                        val message: WaveWsMessage = waveWsMessageLens(it)
                        println("Got a request" + message)
                        when (message.action) {
                            "start" -> raceActions.startRace()
                            "clear" -> raceActions.clear()
                            "reset" -> raceActions.resetRace()
                            "move" -> raceActions.move()
                        }
                        val wsResponse = WsUtils.getMapData(storagePort)
                        val returnMessage = WsMessage(wsResponse)
                        actionSocketLocal.send(returnMessage)
                        sendMessage("action complete  ${message.action}")
                    }
                    actionSocketLocal.onClose {
                        println("action socket is closing")
                    }
                }
            }
        )
        return ws.withReporting(events)
    }

    private fun handleAddPiece(message: WsMessage,raceActions: RaceActions, storagePort: StoragePort) {
        raceActions.addPiece(pieceLocation = addPieceWsMessageLens(message).toPieceLocation())
        returnMapData(storagePort, "add piece")
    }


    private fun returnMapData(storagePort: StoragePort, actionMessage: String) {
        val wsResponse = WsUtils.getMapData(storagePort)
        val returnMessage = WsMessage(wsResponse)
        wsMessageSocket?.send(returnMessage)
        sendMessage("action complete: $actionMessage")
    }

    private fun sendMessage(message: String) {
        wsMessageSocket?.send(WsMessage("""{ "message":"action complete  $message" }"""))
    }

    private fun returnMessage(message: String) = WsResponse { ws: Websocket ->
        ws.send(WsMessage(message))
        ws.close()
    }
}