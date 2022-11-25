package com.dhorby.wavemapper

import org.http4k.core.HttpHandler
import org.http4k.core.then
import org.http4k.filter.DebuggingFilters
import org.http4k.server.SunHttp
import org.http4k.server.asServer


object WaveMapperHttp4kApp {

    @JvmStatic
    fun main(args: Array<String>) {
        val printingApp: HttpHandler = DebuggingFilters.PrintRequest().then(WaveServiceRoutes())

        val server = printingApp.asServer(SunHttp(8080)).start()

        println("Server started on http://localhost:" + server.port())
    }

}


