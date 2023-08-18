package com.dhorby.wavemapper

import org.http4k.core.Request
import org.http4k.lens.Path
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse


object WebSocketRoutes {

    val namePath = Path.of("name")

    val ws: RoutingWsHandler = websockets(
        "/message/{name}" bind { req: Request ->
            WsResponse { ws: Websocket ->
                val name = namePath(req)
                println("Got message from $name")
                ws.send(WsMessage("hello $name"))
                ws.close()
//                ws.onMessage {
//                    ws.send(WsMessage("$name is responding"))
//                }
//                ws.onClose { println("$name is closing") }
            }
        }
    )
}