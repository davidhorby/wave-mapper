package com.dhorby.wavemapper

import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.DataStorage
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.handlers.WaveHandlers
import org.http4k.core.Request
import org.http4k.events.Event
import org.http4k.format.Gson.asJsonObject
import org.http4k.lens.Path
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

class WsEvent(val actor:String) : Event {
    operator fun invoke() = println("$actor")
}



class WebSocketRoutes(
    val siteListFunction: SiteListFunction,
    val dataForSiteFunction: DataForSiteFunction,
    val dataStorage: DataStorage
) {

    val namePath = Path.of("name")

    val ws: RoutingWsHandler = websockets(
        "/message/{name}" bind { req: Request ->
            WsResponse { ws: Websocket ->
                val name = namePath(req)
                val waveDataOnly = dataStorage.getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/move" bind {
            dataStorage.getKeysOfKind(PieceType.BOAT)
                .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
                .forEach(dataStorage::write)
            WsResponse { ws: Websocket ->
                val waveDataOnly = dataStorage.getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/start" bind { req: Request ->
            startRace()
            WsResponse { ws: Websocket ->
                startRace(ws)
            }
        }
    )

    private fun startRace(ws: Websocket) {
        val waveDataOnly =
            dataStorage.getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
        val message = WsMessage(waveDataOnly.asJsonObject().toString())
        ws.send(message)
        ws.close()
    }

    fun startRace() {
        dataStorage.write(WaveHandlers.start)
        dataStorage.write(WaveHandlers.finish)
        dataStorage.getKeysOfKind(PieceType.BOAT)
            .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
            .forEach(dataStorage::write)
    }
}