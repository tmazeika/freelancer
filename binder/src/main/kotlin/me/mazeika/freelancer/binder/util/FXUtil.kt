package me.mazeika.freelancer.binder.util

import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.Property
import javafx.collections.ObservableList

fun <A, B, R> bind(
    transform: (A, B) -> R,
    a: Property<A>,
    b: Property<B>,
): ObjectBinding<R> =
    Bindings.createObjectBinding({ transform(a.value, b.value) }, a, b)

fun <T, R> Property<T>.map(transform: (T) -> R): ObjectBinding<R> =
    Bindings.createObjectBinding({ transform(this.value) }, this)

fun <T> MutableList<out T>.bindContent(source: ObservableList<out T>) =
    Bindings.bindContent(this, source)