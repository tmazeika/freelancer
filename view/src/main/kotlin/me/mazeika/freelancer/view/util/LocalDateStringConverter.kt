package me.mazeika.freelancer.view.util

import javafx.util.StringConverter
import javafx.util.converter.LocalDateStringConverter
import java.time.LocalDate
import java.time.chrono.Chronology
import java.time.format.DateTimeParseException
import java.time.format.FormatStyle
import java.util.*

class LocalDateStringConverter(locale: Locale, chronology: Chronology) :
    StringConverter<LocalDate?>() {

    private val converter =
        LocalDateStringConverter(FormatStyle.SHORT, locale, chronology)

    override fun toString(localTime: LocalDate?): String? =
        converter.toString(localTime)

    override fun fromString(text: String?): LocalDate? = try {
        converter.fromString(text)
    } catch (e: DateTimeParseException) {
        null
    }
}
