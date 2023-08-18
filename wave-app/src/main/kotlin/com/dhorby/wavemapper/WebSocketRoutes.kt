package com.dhorby.wavemapper

import org.http4k.core.Request
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse


object WebSocketRoutes {

    val ws: RoutingWsHandler = websockets(
        "/{name}" bind { req: Request ->
            WsResponse { ws: Websocket ->
                val name = "David"
                ws.send(WsMessage("hello $name"))
                ws.onMessage {
                    ws.send(WsMessage("$name is responding"))
                }
                ws.onClose { println("$name is closing") }
            }
        }
    )
}