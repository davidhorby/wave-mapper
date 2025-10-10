package com.dhorby.wavemapper.filters

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TracingFilter : Filter {
    companion object {
        val LOG: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun invoke(handler: HttpHandler): HttpHandler =
        { request ->
            LOG.info("Got a request ${request.uri}")
            handler.invoke(request)
        }
}
