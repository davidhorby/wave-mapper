package com.dhorby.wavemapper.filters

import org.http4k.websocket.WsFilter
import org.http4k.websocket.WsHandler

class WebSocketFilter:WsFilter {
    override fun invoke(p1: WsHandler): WsHandler = {
        println("Hellooo" + p1.toString())
        p1.invoke(it)
    }
}