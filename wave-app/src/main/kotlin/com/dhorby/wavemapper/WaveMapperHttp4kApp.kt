package com.dhorby.wavemapper

import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object WaveMapperHttp4kApp {



    private val LOG: Logger = LoggerFactory.getLogger(WaveMapperHttp4kApp::class.java)


    @JvmStatic
    fun main(args: Array<String>) {

        LOG.info("Starting the Wave Mapper App")

        val server = WaveServiceRoutes().asServer(Jetty(8080)).start()

        println("Server started on " + server.port())
    }

}


