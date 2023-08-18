package com.dhorby.wavemapper

import DataStoreClient
import DataStoreClient.writeToDatastore
import com.dhorby.gcloud.model.PieceType
import com.dhorby.gcloud.wavemapper.*
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.wavemapper.handlers.WaveHandlers
import com.google.cloud.datastore.Entity
import org.http4k.core.Request
import org.http4k.format.Gson.asJsonObject
import org.http4k.lens.Path
import org.http4k.routing.RoutingWsHandler
import org.http4k.routing.websockets
import org.http4k.routing.ws.bind
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse


class WebSocketRoutes(val siteListFunction: SiteListFunction, val dataForSiteFunction: DataForSiteFunction) {

    val namePath = Path.of("name")

    val ws: RoutingWsHandler = websockets(
        "/message/{name}" bind { req: Request ->
            WsResponse { ws: Websocket ->
                val name = namePath(req)
                println("Got message from $name")
                val waveDataOnly = getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/move" bind { req: Request ->
            DataStoreClient.getKeysOfKind("PieceLocation", PieceType.BOAT).map(Entity::toPieceLocation)
                .map { pieceLocation -> pieceLocation.copy(geoLocation = sailMove(pieceLocation.geoLocation)) }
                .forEach(::writeToDatastore)
            WsResponse { ws: Websocket ->
                val waveDataOnly = getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        },
        "/start" bind { req: Request ->
            startRace()
            WsResponse { ws: Websocket ->
                val waveDataOnly = getAllWaveDataWithPieces(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        }
    )

    fun startRace() {
        writeToDatastore(WaveHandlers.start)
        writeToDatastore(WaveHandlers.finish)
        DataStoreClient.getKeysOfKind("PieceLocation", PieceType.BOAT).map(Entity::toPieceLocation)
            .map { it.copy(geoLocation = WaveHandlers.start.geoLocation) }
            .forEach(::writeToDatastore)
    }
}