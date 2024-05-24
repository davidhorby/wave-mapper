package com.dhorby.wavemapper.endpoint

import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import java.util.*

fun Properties(): HttpHandler = {
    val properties: Properties = System.getProperties()
    val allProperties = properties.filter { it.key != null }.map {
        it.key.toString() + ":" + it.value
    }.joinToString("</br>")
    Response(Status.OK).body(allProperties)
}