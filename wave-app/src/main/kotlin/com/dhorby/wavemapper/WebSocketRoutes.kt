package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.actions.RaceActions
import com.dhorby.wavemapper.actions.ResetRace
import com.dhorby.wavemapper.actions.StartRace
import com.dhorby.wavemapper.port.StoragePort
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
    private val storagePort: StoragePort
) {
    val raceActions = RaceActions(storagePort)

    val namePath = Path.of("name")

    val ws: RoutingWsHandler = websockets(
        "/message/{name}" bind {
            WsResponse { ws: Websocket ->
//                val name = namePath(req)
                val waveDataOnly = storagePort.getLocationData().toGoogleMapFormatList()
                returnMessage(waveDataOnly.asJsonObject().toString())
            }
        },
        "/move" bind {
            storagePort.getKeysOfType(PIECE_LOCATION,PieceType.BOAT)
                .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
                .forEach(storagePort::write)
            WsResponse { ws: Websocket ->
                val waveDataOnly = storagePort.getLocationData().toGoogleMapFormatList()
                returnMessage(waveDataOnly.asJsonObject().toString())
            }
        },
        "/start" bind {
            WsResponse { ws: Websocket ->
                startRace(ws)
            }
            StartRace(raceActions)
            returnMessage("Success")
        },
        "/clear" bind {
            raceActions.clear()
            returnMessage("Success")
        },
        "/reset" bind {
            ResetRace(raceActions)
            returnMessage("Success")
        }
    )

    private fun returnMessage(message: String) = WsResponse { ws: Websocket ->
        ws.send(WsMessage(message))
        ws.close()
    }

    private fun startRace(ws: Websocket) {
        val waveDataOnly = storagePort.getLocationData().toGoogleMapFormatList()
        val message = WsMessage(waveDataOnly.asJsonObject().toString())
        ws.send(message)
        ws.close()
    }
}