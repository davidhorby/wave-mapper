package com.dhorby.wavemapper

import com.dhorby.wavemapper.model.DatePeriod
import com.dhorby.wavemapper.model.Location
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper.builder
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

val siteList: List<String> = listOf(
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
    "162090",
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

object XMLWaveParser {

    fun getWaveDataAsJson(metOfficeApiKey: String): String {
        val allData: List<Location> = getAllWaveData(metOfficeApiKey)
        val objectMapper: ObjectMapper = builder()
            .addModule(JavaTimeModule())
            .build()
        val waveDataAsJson: String = objectMapper.writeValueAsString(allData)
        return waveDataAsJson
    }

    internal fun getAllWaveData(metOfficeApiKey: String): List<Location> {

        return siteList.map { site ->
            val url =
                "http://datapoint.metoffice.gov.uk/public/data/val/wxmarineobs/all/xml/$site?res=3hourly&key=$metOfficeApiKey"
            getWaveDataForSite(url)
        }.filter { !it.id.isNullOrEmpty() }

    }

    private fun getWaveDataForSite(url: String): Location {
        val urlReal: URL = URL(url)
        val xmlText = urlReal.readText()
        val xmlMapper = XmlMapper()
        val jsonNode: JsonNode = xmlMapper.readTree(xmlText)
        return getLocation(jsonNode)
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
                periods.map { period ->
                    val waveHeight: Float = getWaveReps(period).maxOrNull() ?: 0.0F
                    val windSpeed: Int = getWindSpeedInKm(period).maxOrNull() ?: 0
                    val windDirection: String = getWindDirection(period).maxOrNull() ?: ""
                    val dateStr: String? = period.get("value").toString()
                    dateStr?.let {
                        val date = it.toString().removeQuotes().parseToDate()
                        date?.let {
                            DatePeriod(it, waveHeight, windSpeed, windDirection)
                        }
                    }
                }.filterNotNull()
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


}
