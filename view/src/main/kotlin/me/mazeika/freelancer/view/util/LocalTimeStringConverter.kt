package me.mazeika.freelancer.view.util

import javafx.util.StringConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class LocalTimeStringConverter(private val formatter: DateTimeFormatter) :
    StringConverter<LocalTime?>() {

    override fun toString(localTime: LocalTime?): String =
        localTime?.format(formatter) ?: ""

    override fun fromString(text: String?): LocalTime? = try {
        LocalTime.parse(text?.trim() ?: "", formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}
