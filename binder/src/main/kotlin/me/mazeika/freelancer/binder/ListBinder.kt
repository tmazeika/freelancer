package me.mazeika.freelancer.binder

import javafx.beans.property.Property
import javafx.collections.ObservableList

interface ListBinder<T> {
    val items: ObservableList<out T>
    val selected: Property<in T>
}
