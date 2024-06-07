package com.dhorby.wavemapper.endpoints.ws

import com.dhorby.wavemapper.endpoints.utils.WsUtils.generateWsResponse
import org.http4k.core.Request
import org.http4k.websocket.WsResponse

object Logging {
    fun Log(message: String): (Request) -> WsResponse = { generateWsResponse(message) }
}