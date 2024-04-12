package com.dhorby.wavemapper.tracing

import org.http4k.core.Request
import org.http4k.routing.RequestWithRoute
import org.http4k.websocket.WsConsumer
import org.http4k.websocket.WsResponse
import java.time.Duration

data class DbTransaction(
    val request: Request,
    val response: WsResponse,
    val duration: Duration,
    val labels: Map<String, String> = when {
        response is WsConsumer -> mapOf(ROUTING_GROUP_LABEL to response.consumer.toString())
        request is RequestWithRoute -> mapOf(ROUTING_GROUP_LABEL to request.xUriTemplate.toString())
        else -> emptyMap()
    }
) {
    fun label(name: String, value: String) = copy(labels = labels + (name to value))
    fun label(name: String) = labels[name]

    val routingGroup = labels[ROUTING_GROUP_LABEL] ?: "UNMAPPED"

    companion object {
        internal const val ROUTING_GROUP_LABEL = "routingGroup"
    }
}