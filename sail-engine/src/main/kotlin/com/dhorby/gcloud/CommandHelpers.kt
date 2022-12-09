package com.dhorby.gcloud

import com.dhorby.gcloud.model.PieceLocation
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

fun String.extractCommand(): Command? {
    val objectMapper = ObjectMapper().registerKotlinModule()
    return objectMapper.readerFor(Command::class.java).readValue<Command>(this)
}

fun String.extractPieceLocation(): PieceLocation {
    val objectMapper = ObjectMapper().registerKotlinModule()
    objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    return objectMapper.readerFor(PieceLocation::class.java).readValue<PieceLocation>(this)
}


