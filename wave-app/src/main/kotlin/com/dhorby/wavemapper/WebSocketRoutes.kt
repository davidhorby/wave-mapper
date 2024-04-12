package com.dhorby.wavemapper

import com.dhorby.gcloud.external.storage.EntityKind.PIECE_LOCATION
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.sailMove
import com.dhorby.wavemapper.actions.resetRace
import com.dhorby.wavemapper.adapter.StorageAdapter
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
    private val storageAdapter: StorageAdapter
) {

    val namePath = Path.of("name")

    val ws: RoutingWsHandler = websockets(
        "/message/{name}" bind {
            WsResponse { ws: Websocket ->
//                val name = namePath(req)
                val waveDataOnly = storageAdapter.getLocationData().toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/move" bind {
            storageAdapter.getKeysOfType(PIECE_LOCATION,PieceType.BOAT)
                .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
                .forEach(storageAdapter::write)
            WsResponse { ws: Websocket ->
                val waveDataOnly = storageAdapter.getLocationData().toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/start" bind {
            WsResponse { ws: Websocket ->
                startRace(ws)
            }
        },
        "/clear" bind {
            storageAdapter.clear(PIECE_LOCATION)
            WsResponse { ws: Websocket ->
                ws.send(WsMessage("Success"))
                ws.close()
            }
        },
        "/reset" bind {
            resetRace(storageAdapter)
            WsResponse { ws: Websocket ->
                ws.send(WsMessage("Success"))
                ws.close()
            }
        }
    )

    private fun startRace(ws: Websocket) {
        val waveDataOnly = storageAdapter.getLocationData().toGoogleMapFormatList()
        val message = WsMessage(waveDataOnly.asJsonObject().toString())
        ws.send(message)
        ws.close()
    }
}