package com.dhorby.wavemapper

import com.dhorby.gcloud.model.GeoLocation
import com.dhorby.gcloud.model.PieceLocation
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
        "/start" bind {
            startRace()
            WsResponse { ws: Websocket ->
                startRace(ws)
            }
        },
        "/clear" bind {
            dataStorage.clear("PieceLocation")
            WsResponse { ws: Websocket ->
                ws.send(WsMessage("Success"))
                ws.close()
            }
        },
        "/reset" bind {

            val startLocation = PieceLocation(
                id = "start",
                name = "Newport",
                geoLocation = GeoLocation(lat = 41.29, lon = -71.19),
                pieceType = PieceType.START
            )
            val finishLocation = PieceLocation(
                id = "finish",
                name = "Lisbon",
                geoLocation = GeoLocation(lat = 38.41, lon = -9.09),
                pieceType = PieceType.START
            )
            val testSharkLocation = PieceLocation(
                id = "1234",
                name = "Sue",
                geoLocation = GeoLocation(lat = 34.45, lon = -49.01),
                pieceType = PieceType.SHARK
            )

            val testBoatLocation = PieceLocation(
                id = "234ea",
                name = "Albert",
                geoLocation = GeoLocation(lat = 39.45, lon = -5.01),
                pieceType = PieceType.BOAT
            )

            val testPirateLocation = PieceLocation(
                id = "pir123",
                name = "Captain Morgan",
                geoLocation = GeoLocation(lat = 20.45, lon = -15.01),
                pieceType = PieceType.PIRATE
            )
            dataStorage.write(startLocation)
            dataStorage.write(finishLocation)
            dataStorage.write(testSharkLocation)
            dataStorage.write(testBoatLocation)
            dataStorage.write(testPirateLocation)
            WsResponse { ws: Websocket ->
                ws.send(WsMessage("Success"))
                ws.close()
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