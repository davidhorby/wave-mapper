package com.dhorby.wavemapper

import com.dhorby.gcloud.model.com.dhorby.gcloud.model.GeoLocation
import com.dhorby.wavemapper.Constants.metOfficeApiKey
import com.dhorby.wavemapper.Constants.metOfficeUrl
import com.dhorby.wavemapper.model.*
import com.fasterxml.jackson.databind.JsonNode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


typealias SiteListFunction = () -> MutableList<Site>
typealias DataForSiteFunction = (site: String) -> WaveLocation?


fun getMetOfficeUrl(site: String): String {
    return "${metOfficeUrl}$site?res=3hourly&key=${metOfficeApiKey}"
}

fun getAllWaveData(
    siteListFunction: SiteListFunction,
    dataForSiteFunction: DataForSiteFunction
): MutableList<WaveLocation> {
    val mapNotNull: List<WaveLocation> = siteListFunction().mapNotNull { site ->
        dataForSiteFunction(site.id)
    }
    return mapNotNull.filter { location ->
        location.id.isNotEmpty()
    }.toMutableList()
}

fun MutableList<Location>.withAddedShark(): MutableList<Location> {
    this.add(
        SharkLocation(
            id = "1234",
            name = "Susan",
            date = LocalDate.now(),
            geoLocation = GeoLocation(lat = 53.506397F, lon = 0.928163F),
            size = 2.1F,
            species = SharkType.GREAT_WHITE
        )
    )
    this.add(
        SharkLocation(
            id = "12345",
            name = "Alan",
            date = LocalDate.now(),
            geoLocation = GeoLocation(lat = 51.108184F, lon = -5.016133F),
            size = 1.1F,
            species = SharkType.HAMMERHEAD
        )
    )
    return this
}

fun MutableList<Location>.withBoat(): MutableList<Location> {
    this.add(
        BoatLocation(
            id = "1234",
            name = "Geoffrey",
            date = LocalDate.now(),
            geoLocation = GeoLocation(lat = 50.500370F, lon = -7.421526F),
            size = 2.1F,
            boattype = BoatType.SAIL
        )
    )
    this.add(
        BoatLocation(
            id = "1234",
            name = "Kate",
            date = LocalDate.now(),
            geoLocation = GeoLocation(lat = 55.169322F, lon = -11.394872F),
            size = 2.1F,
            boattype = BoatType.SAIL
        )
    )
    return this
}

fun JsonNode.getSiteLocations(): List<Site> {
    return this.getLocationPath().map {
        Site(
            id = it.path("id").textValue(),
            geoLocation = GeoLocation(
                lat = it.path("latitude").floatValue(),
                lon = it.path("longitude").floatValue()
            ),
            name = it.path("name").textValue(),
            obsLocationType = it.path("obsLocationType").textValue(),
            obsRegion = it.path("obsRegion").textValue(),
            obsSource = it.path("obsSource").textValue()
        )
    }
}

fun JsonNode.getLocation(): WaveLocation {
    val id: String = this.getDataValue().getLocationPath().path("i")?.toString()?.removeQuotes() ?: "Unknown"
    val name: String =
        this.getDataValue().getLocationPath().path("name")?.toString()?.removeQuotes() ?: "Unknown name"
    val lat: Float = this.getDataValue().getLocationPath().path("lat")?.toString()?.parseToFloat() ?: 0.0F
    val lon: Float = this.getDataValue().getLocationPath().path("lon")?.toString()?.parseToFloat() ?: 0.0F
    val waveDataReadings: List<WaveDataReading> = getDatePeriods(this)
    return WaveLocation(
        id = id,
        name = name,
        geoLocation = GeoLocation(lat = lat, lon = lon),
        waveDataReadings = waveDataReadings
    )
}

private fun getDatePeriods(jsonNode: JsonNode): List<WaveDataReading> {
    val periods: JsonNode = jsonNode.getDataValue().getLocationPath().path("Period")
    return when {
        periods.isArray -> {
            periods.mapNotNull { period ->
                val waveHeight: Float = getWaveReps(period).maxOrNull() ?: 0.0F
                val windSpeed: Int = getWindSpeedInKm(period).maxOrNull() ?: 0
                val windDirection: String = getWindDirection(period).maxOrNull() ?: ""
                val dateStr: String = period.get("value").toString()

                dateStr.removeQuotes().parseToDate()?.let {
                    WaveDataReading(it, waveHeight, windSpeed, windDirection)
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
    if (this.isEmpty()) return 0.0F
    else
        return try {
            this.removeQuotes().toFloat()
        } catch (e: Exception) {
            println("Error parsing $this")
            return 0.0F
        }
}

private fun String.parseToInt(): Int {
    if (this.isEmpty()) return 0
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

fun Float.mapWaveHeightToIcon(): String {
    return when {
        this > 0.0 && this <= 0.4 -> "verysmall"
        this > 0.4 && this <= 0.8 -> "small"
        this > 0.8 && this <= 1.0 -> "medium"
        this > 1.0 && this <= 3.0 -> "big"
        this >= 3.0 -> "verybig"
        else -> "notavailable"
    }
}

private fun JsonNode.getDataValue() = this.path("DV")
private fun JsonNode.getLocationPath() = this.path("Location")






