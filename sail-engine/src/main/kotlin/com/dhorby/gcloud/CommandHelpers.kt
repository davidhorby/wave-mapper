package com.dhorby.gcloud

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun String.extractCommand(): Command? {
    val objectMapper = ObjectMapper().registerKotlinModule()
    return objectMapper.readerFor(Command::class.java).readValue<Command>(this)
}
