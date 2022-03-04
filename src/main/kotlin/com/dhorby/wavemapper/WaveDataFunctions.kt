package com.dhorby.wavemapper

import com.dhorby.wavemapper.Constants.Companion.metOfficeApiKey
import com.dhorby.wavemapper.Constants.Companion.metOfficeUrl
import com.dhorby.wavemapper.model.DatePeriod
import com.dhorby.wavemapper.model.Location
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


typealias SiteListFunction  = () -> List<String>
typealias Site = String

val siteListFunction:SiteListFunction = {
    listOf(
        "162103",
        "162305",
        "162001",
        "162029",
        "162081",
        "162105",
        "162163",
        "162304",
        "164045",
        "164046",
        "162027",
//    "162090",
        "162091",
        "162092",
        "162093",
        "162094",
        "162095",
        "162107",
        "162170",
        "162442",
        "162050",
        "162030"
    )
}
val objectMapper: ObjectMapper = JsonMapper.builder()
    .addModule(JavaTimeModule())
    .build()


fun List<Location>.asJson():String = objectMapper.writeValueAsString(this)

fun getMetOfficeUrl(site:String):String {
    return "${metOfficeUrl}$site?res=3hourly&key=$metOfficeApiKey"
}

fun getAllWaveData(siteList: SiteListFunction): List<Location> = siteList().mapNotNull { site: String ->
    getWaveDataForSite(site)
}.filter { location ->
    location.id.isNotEmpty()
}

fun getWaveDataForSite(site:Site): Location? = try {
    val metOfficeUrl = getMetOfficeUrl(site)
    URL(metOfficeUrl).readText()
    val xmlText = URL(metOfficeUrl).readText()
    val xmlMapper = XmlMapper()
    val jsonNode: JsonNode = xmlMapper.readTree(xmlText)
    getLocation(jsonNode)
} catch (ex: Exception) {
    println("Failed to read url ${URL(metOfficeUrl)} ${ex.message}")
    null
}

private fun getLocation(jsonNode: JsonNode): Location {
    val id: String = jsonNode.getDataValue().getLocation().path("i")?.toString()?.removeQuotes() ?: "Unknown"
    val name: String =
        jsonNode.getDataValue().getLocation().path("name")?.toString()?.removeQuotes() ?: "Unknown name"
    val lat: Float = jsonNode.getDataValue().getLocation().path("lat")?.toString()?.parseToFloat() ?: 0.0F
    val lon: Float = jsonNode.getDataValue().getLocation().path("lon")?.toString()?.parseToFloat() ?: 0.0F
    val datePeriods: List<DatePeriod> = getDatePeriods(jsonNode)
    return Location(
        id = id,
        name = name,
        lat = lat,
        lon = lon,
        datePeriods = datePeriods
    )
}

private fun getDatePeriods(jsonNode: JsonNode): List<DatePeriod> {
    val periods: JsonNode = jsonNode.getDataValue().getLocation().path("Period")
    return when {
        periods.isArray -> {
            periods.mapNotNull { period ->
                val waveHeight: Float = getWaveReps(period).maxOrNull() ?: 0.0F
                val windSpeed: Int = getWindSpeedInKm(period).maxOrNull() ?: 0
                val windDirection: String = getWindDirection(period).maxOrNull() ?: ""
                val dateStr: String = period.get("value").toString()

                dateStr.removeQuotes().parseToDate()?.let {
                    DatePeriod(it, waveHeight, windSpeed, windDirection)
                }
            }
        }
        else -> emptyList()
    }
}

private fun getWaveReps(jsonNode: JsonNode): List<Float> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        val waveHeight = rep.get("Wh")?.toString()?.parseToFloat()
        waveHeight
    }.filterNotNull()
}

private fun getWindSpeedInKm(jsonNode: JsonNode): List<Int> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        val waveHeight = rep.get("S")?.toString()?.parseToInt()
        val waveHeightKph: Double = waveHeight?.let {
            it * 1.85
        } ?: 0.0
        waveHeightKph.roundToInt()
    }
}

private fun getWindDirection(jsonNode: JsonNode): List<String> {
    val reps: JsonNode = jsonNode.path("Rep")
    return reps.map { rep ->
        rep.get("D")?.toString()?.removeQuotes() ?: ""
    }
}


private fun String.parseToFloat(): Float {
    if (this.isNullOrEmpty()) return 0.0F
    else
        return try {
            this.removeQuotes().toFloat()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0.0F
        }
}

private fun String.parseToInt(): Int {
    if (this.isNullOrEmpty()) return 0
    else
        return try {
            this.removeQuotes().toInt()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0
        }
}

fun String.removeQuotes(): String = this.toString().replace("\"", "")
fun String.parseToDate(): LocalDate? {
    // 2021-06-24Z
    val dFormat = DateTimeFormatter.ofPattern("yyyy-MM-ddX")
    return LocalDate.parse(this, dFormat)
}

fun Float.mapWaveHeight(): String {
    return when {
        this <= 0.4 -> "verysmall"
        this > 0.4 && this <= 0.8 -> "small"
        this > 0.8 && this <= 1.0 -> "medium"
        this > 1.0 && this <= 3.0 -> "big"
        this >= 3.0 -> "verybig"
        else -> "unknown"
    }
}

private fun JsonNode.getDataValue() = this.path("DV")
private fun JsonNode.getLocation() = this.path("Location")






