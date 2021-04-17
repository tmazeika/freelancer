package me.mazeika.freelancer.view.services

import javafx.beans.property.ReadOnlyObjectProperty
import java.time.Instant

interface TimeService {
    /**
     * Holds the current system time, updated every second as close to the start
     * of the second as possible.
     */
    val nowProperty: ReadOnlyObjectProperty<Instant>
}
