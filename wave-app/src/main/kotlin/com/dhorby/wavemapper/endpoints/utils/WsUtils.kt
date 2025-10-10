package com.dhorby.wavemapper.endpoints.utils

import com.dhorby.gcloud.wavemapper.datautils.toGoogleMapFormatList
import com.dhorby.wavemapper.port.StoragePort
import org.http4k.format.Gson.asJsonObject
import org.http4k.websocket.Websocket
import org.http4k.websocket.WsMessage
import org.http4k.websocket.WsResponse

object WsUtils {
    fun generateWsResponse(message: String) =
        WsResponse { ws: Websocket ->
            ws.send(WsMessage(message))
            ws.close()
        }

    fun getMapData(storagePort: StoragePort): String =
        storagePort
            .getLocationData()
            .toGoogleMapFormatList()
            .asJsonObject()
            .toString()
}
