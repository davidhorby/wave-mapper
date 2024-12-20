package com.dhorby.wavemapper

import com.dhorby.gcloud.model.Location
import com.dhorby.gcloud.model.WaveLocation
import com.dhorby.wavemapper.model.AddPieceWsMessage
import org.http4k.core.Body
import org.http4k.format.Jackson.auto
import org.http4k.websocket.WsMessage


val waveLocationBodyLens = Body.auto<WaveLocation>().toLens()
val waveLocationListBodyLens = Body.auto<List<Location>>().toLens()
val addPieceWsMessageLens = WsMessage.auto<AddPieceWsMessage>().toLens()
