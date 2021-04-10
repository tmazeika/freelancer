package me.mazeika.freelancer.binder

import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList

interface ListBinder<T> {
    val items: ObservableList<out T>
    val selected: ObjectProperty<in T>
}
