package me.mazeika.freelancer.view.util

import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty

object BidiBindings {
    fun <A, B> bind(
        a: Property<A>,
        b: Property<B>,
        converter: Converter<A, B>
    ) {
        a.value = converter.bToA(b.value)
        a.addListener { _, _, new ->
            b.value = converter.aToB(new)
        }
        b.addListener { _, _, new ->
            a.value = converter.bToA(new)
        }
    }

    fun <A, B> createProperty(
        b: Property<B>,
        converter: Converter<A, B>
    ): Property<A> = SimpleObjectProperty<A>().also { a ->
        bind(a, b, converter)
    }

    interface Converter<A, B> {
        fun aToB(a: A): B

        fun bToA(b: B): A
    }
}
