package com.dhorby.wavemapper

import com.dhorby.gcloud.wavemapper.DataForSiteFunction
import com.dhorby.gcloud.wavemapper.SiteListFunction
import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.gcloud.wavemapper.getAllWaveData
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
                val waveDataOnly = getAllWaveData(siteListFunction, dataForSiteFunction).toGoogleMapFormatList()
                val message =  WsMessage(waveDataOnly.asJsonObject().toString())
                ws.send(message)
                ws.close()
            }
        }
    )
}