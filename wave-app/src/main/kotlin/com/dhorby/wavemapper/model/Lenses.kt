package com.dhorby.wavemapper.model

import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.websocket.WsMessage

object Lenses {

    val addPieceWsMessageLens = WsMessage.auto<AddPieceWsMessage>().toLens()

    fun tt() {
        
    }
}