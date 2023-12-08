package com.dhorby.wavemapper

import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object WaveMapperHttp4kApp {



    private val LOG: Logger = LoggerFactory.getLogger(WaveMapperHttp4kApp::class.java)

//    val events = TracerBulletEvents(
//        listOf(HttpTracer(actor)), // A tracer to capture HTTP calls
//        listOf(PumlSequenceDiagram), // Render the HTTP traffic as a PUML diagram
//        TraceRenderPersistence.FileSystem(File(".")) // Store the result
//    )

    @JvmStatic
    fun main(args: Array<String>) {

        LOG.info("Starting the Wave Mapper App")

        val server = WaveServiceRoutes().asServer(Jetty(8080)).start()

        println("Server started on " + server.port())
    }

}


