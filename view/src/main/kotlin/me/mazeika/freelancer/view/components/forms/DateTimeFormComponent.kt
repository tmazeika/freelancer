package me.mazeika.freelancer.view.components.forms

import javafx.beans.property.BooleanProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Node
import javafx.scene.control.DatePicker
import javafx.scene.control.Hyperlink
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import me.mazeika.freelancer.binder.util.bind
import me.mazeika.freelancer.binder.util.mapBoolean
import me.mazeika.freelancer.view.util.LocalDateStringConverter
import me.mazeika.freelancer.view.util.LocalTimeStringConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.chrono.Chronology
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import java.util.*

class DateTimeFormComponent(
    override val label: String,
    value: Property<Instant>,
    zone: ZoneId,
    locale: Locale,
) : HBox(), FormComponent {

    val emptyProperty: BooleanProperty = SimpleBooleanProperty()

    init {
        value.value = value.value?.truncatedTo(ChronoUnit.SECONDS)
        val timeFormatter =
            DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)
                .withLocale(locale)
        val dateProp =
            SimpleObjectProperty(value.value?.let {
                LocalDate.ofInstant(it, zone)
            })
        val timeProp =
            SimpleObjectProperty(value.value?.let {
                LocalTime.ofInstant(it, zone)
            })
        val datePicker =
            DatePicker(value.value?.atZone(zone)?.toLocalDate()).apply {
                chronology = Chronology.ofLocale(locale)
                converter = LocalDateStringConverter(locale, chronology)
                editor.textProperty().bindBidirectional(dateProp, converter)
            }
        val timePicker = TextField().apply {
            textProperty().bindBidirectional(
                timeProp,
                LocalTimeStringConverter(timeFormatter)
            )
        }
        value.bind(bind({ date, time ->
            if (date == null || time == null) null else {
                time.atDate(date).atZone(zone).toInstant()
            }
        }, dateProp, timeProp))
        emptyProperty.bind(
            datePicker.editor.textProperty().mapBoolean { it.isNullOrBlank() }
                .and(
                    timePicker.textProperty().mapBoolean { it.isNullOrBlank() })
        )
        spacing = 10.0
        children.setAll(
            datePicker,
            timePicker,
            Hyperlink("Now").apply {
                visitedProperty().bind(SimpleBooleanProperty(false))
                setOnAction {
                    val now = Instant.now().truncatedTo(ChronoUnit.SECONDS)
                    dateProp.value = now.atZone(zone).toLocalDate()
                    timeProp.value = now.atZone(zone).toLocalTime()
                }
            },
        )
    }

    override val node: Node = this
}
