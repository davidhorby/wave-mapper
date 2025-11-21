package com.dhorby.wavemapper.model

import model.GeoLocation
import model.PieceLocation
import model.PieceType
import org.http4k.contract.openapi.OpenAPIJackson.auto
import org.http4k.websocket.WsMessage

data class WaveWsMessage(
    val action: String,
    val counter: Int?,
)

data class WaveResponseWsMessage(
    val message: String,
    val counter: Int?,
)

data class AddPieceWsMessage(
    val name: String,
    val pieceType: String,
    val lat: Double,
    val lon: Double,
)

fun AddPieceWsMessage.toPieceLocation(): PieceLocation =
    PieceLocation(
        id = this.name,
        name = this.name,
        pieceType = PieceType.valueOf(this.pieceType),
        geoLocation = GeoLocation(lat = this.lat, lon = this.lon),
    )

val waveWsMessageLens = WsMessage.auto<WaveWsMessage>().toLens()
val waveResponseWsMessageLens = WsMessage.auto<WaveResponseWsMessage>().toLens()
