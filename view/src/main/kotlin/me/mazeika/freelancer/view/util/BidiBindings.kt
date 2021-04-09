package me.mazeika.freelancer.view.util

import javafx.beans.property.Property

object BidiBindings {

    fun <A, B> bind(
        a: Property<A>,
        b: Property<B>,
        to: (A) -> B,
        from: (B) -> A,
    ): Property<A> {
        a.value = from(b.value)
        a.addListener { _, _, a2 -> b.value = to(a2) }
        b.addListener { _, _, b2 -> a.value = from(b2) }
        return a
    }

    fun <A, B> bind(
        a: Property<A>,
        b: Property<B>,
        converter: Converter<A, B>,
    ): Property<A> = bind(a, b, converter::to, converter::from)

    interface Converter<A, B> {

        fun to(a: A): B

        fun from(b: B): A
    }
}
