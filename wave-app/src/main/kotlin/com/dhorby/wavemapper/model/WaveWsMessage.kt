package com.dhorby.wavemapper.model

import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.websocket.WsMessage

data class WaveWsMessage(
    val action:String,
    val counter: Int?
)

data class WaveResponseWsMessage(
    val message:String,
    val counter: Int?
)

data class AddPieceWsMessage(
    val name:String,
    val pieceType: String,
    val lat:Long,
    val lon:Long
)

val waveWsMessageLens = WsMessage.auto<WaveWsMessage>().toLens()
val waveResponseWsMessageLens = WsMessage.auto<WaveResponseWsMessage>().toLens()
val addPieceWsMessageLens = WsMessage.auto<AddPieceWsMessage>().toLens()
