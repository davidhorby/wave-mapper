package com.dhorby.wavemapper.endpoints.ws

import org.http4k.core.Request
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

object Logging {

    fun Log(message: String): (Request) -> WsResponse = { generateWsResponse(message) }

    private fun generateWsResponse(message: String): WsResponse {
        return WsResponse { ws: Websocket ->
            ws.send(WsMessage(message))
            ws.close()
        }
    }
}