package com.dhorby.wavemapper.endpoints.http

import com.dhorby.wavemapper.model.Wave
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.ResourceLoader
import org.http4k.template.HandlebarsTemplates

@Suppress("ktlint:standard:function-naming")
fun DataSheet(): HttpHandler =
    {
        val (renderer, _) = buildResourceLoaders(false)
        val viewModel = Wave("M5", 2.1.toLong())
        Response(Status.OK).body(renderer(viewModel))
    }

private fun buildResourceLoaders(hotReload: Boolean) =
    when {
        hotReload -> HandlebarsTemplates().HotReload("./src/main/resources") to ResourceLoader.Classpath("public")
        else -> HandlebarsTemplates().CachingClasspath() to ResourceLoader.Classpath("public")
    }
