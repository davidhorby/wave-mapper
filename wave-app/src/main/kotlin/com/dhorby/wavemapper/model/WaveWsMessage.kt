package com.dhorby.wavemapper.model

import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.websocket.WsMessage

data class WaveWsMessage(val message:String, val counter:Int?)

val waveWsMessageLens = WsMessage.auto<WaveWsMessage>().toLens()