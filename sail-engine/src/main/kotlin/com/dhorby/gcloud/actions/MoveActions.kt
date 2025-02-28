package com.dhorby.gcloud.actions

import kotlinx.serialization.json.Json


inline fun <reified T> String.jsonToObject():T = Json.decodeFromString(this)
