package me.mazeika.freelancer.binder.admin

import javafx.beans.property.ObjectProperty
import javafx.collections.ObservableList

interface EntityAdmin<T> {

    val entities: ObservableList<out T>
    val selected: ObjectProperty<in T>
}
