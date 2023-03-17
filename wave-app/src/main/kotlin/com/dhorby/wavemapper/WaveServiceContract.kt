package com.dhorby.wavemapper

import org.http4k.contract.contract
import org.http4k.contract.meta
import org.http4k.contract.openapi.ApiInfo
import org.http4k.contract.openapi.v3.ApiServer
import org.http4k.contract.openapi.v3.OpenApi3
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.Uri
import org.http4k.format.Argo

object WaveServiceContract {

    const val apiRoot = "/api"
    val PROPERTIES_PATH_NODE = "/properties"

    private val waveMapperRoot = apiRoot

    val contract = contract {
        renderer = OpenApi3(
            ApiInfo("my great api", "v1.0"),
            Argo,
            servers = listOf(ApiServer(Uri.of("http://localhost:8000"), "the greatest server"))
        )
        descriptionPath = "/docs/openapi.json"

        routes += "/ping" meta {
            summary = "health check"
            description = "Check if the service is alive"
            returning(OK to "The result")
        } bindContract Method.GET to { _ -> Response(OK).body("pong") }
//
//        routes += "/add" / Path.int().of("value1") / Path.int().of("value2") meta {
//            summary = "add"
//            description = "Adds 2 numbers together"
//            returning(OK to "The result")
//        } bindContract GET to ::add
//
//        // note here that the trailing parameter can be ignored - it would simply be the value "divide".
//        routes += Path.int().of("value1") / Path.int().of("value2") / "divide" meta {
//            summary = "divide"
//            description = "Divides 2 numbers"
//            returning(OK to "The result")
//        } bindContract GET to { first, second, _ ->
//            { Response(OK).body((first / second).toString()) }
//        }
//
//        routes += "/echo" / Path.of("name") meta {
//            summary = "echo"
//            queries += ageQuery
//        } bindContract GET to ::echo
    }
}