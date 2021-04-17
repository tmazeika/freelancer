package me.mazeika.freelancer.binder.util

import com.google.common.base.Converter
import com.sun.javafx.binding.Logging
import javafx.beans.WeakListener
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.Property
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableSet
import javafx.collections.SetChangeListener
import java.lang.ref.WeakReference

fun <A, B, R> bind(
    transform: (A, B) -> R,
    a: ObservableValue<A>,
    b: ObservableValue<B>,
): ObjectBinding<R> =
    Bindings.createObjectBinding({ transform(a.value, b.value) }, a, b)

fun <T, R> ObservableValue<T>.map(transform: (T) -> R): ObjectBinding<R> =
    Bindings.createObjectBinding({ transform(this.value) }, this)

fun <T> ObservableValue<T>.mapBoolean(predicate: (T) -> Boolean): BooleanBinding =
    Bindings.createBooleanBinding({ predicate(this.value) }, this)

fun <A, B> Property<A>.bindBidirectional(
    other: Property<B>,
    converter: Converter<B, A>
) {
    require(this !== other)
    val binding = ConversionBidirectionalBinding(this, other, converter)
    this.value = converter.convert(other.value)
    this.addListener(binding)
    other.addListener(binding)
}

abstract class BidirectionalBinding<T> constructor(
    property1: Any,
    property2: Any
) : ChangeListener<T>, WeakListener {
    abstract val property1: Any?
    abstract val property2: Any?

    private val cachedHashCode: Int =
        property1.hashCode() * property2.hashCode()

    override fun wasGarbageCollected(): Boolean =
        property1 == null || property2 == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (
            property1 == null || property2 == null || other !is BidirectionalBinding<*>
        ) {
            return false
        }
        return when {
            other.property1 == null || other.property2 == null -> false
            property1 === other.property1 && property2 === other.property2 -> true
            property1 === other.property2 && property2 === other.property1 -> true
            else -> false
        }
    }

    override fun hashCode(): Int = cachedHashCode
}

class ConversionBidirectionalBinding<T, R>(
    property1: Property<R>,
    property2: Property<T>,
    private val converter: Converter<T, R>
) : BidirectionalBinding<Any?>(property1, property2) {

    override val property1: Any
        get() = property1Ref.get()!!

    override val property2: Any
        get() = property2Ref.get()!!

    private val property1Ref: WeakReference<Property<R>> =
        WeakReference(property1)
    private val property2Ref: WeakReference<Property<T>> =
        WeakReference(property2)

    private var updating = false

    override fun changed(
        observable: ObservableValue<out Any?>?,
        oldValue: Any?,
        newValue: Any?
    ) {
        if (!updating) {
            val property1 = property1Ref.get()
            val property2 = property2Ref.get()
            if (property1 == null || property2 == null) {
                property1?.removeListener(this)
                property2?.removeListener(this)
            } else {
                try {
                    updating = true
                    if (property1 === observable) {
                        try {
                            property2.value =
                                converter.reverse().convert(property1.value)
                        } catch (e: Exception) {
                            Logging.getLogger().warning(
                                "Exception while converting in bidirectional binding",
                                e
                            )
                            property2.value = null
                        }
                    } else {
                        try {
                            property1.value = converter.convert(property2.value)
                        } catch (e: Exception) {
                            Logging.getLogger().warning(
                                "Exception while converting in bidirectional binding",
                                e
                            )
                            property1.value = null
                        }
                    }
                } finally {
                    updating = false
                }
            }
        }
    }
}

fun <T, R> MutableSet<R>.bindContent(
    source: ObservableSet<T>,
    transform: (T) -> R
) {
    require(this !== source)
    val contentBinding = MappedSetContentBinding(this, transform)
    this.clear()
    this.addAll(source.map(transform))
    source.removeListener(contentBinding)
    source.addListener(contentBinding)
}

fun <T, R> MutableList<R>.bindContent(
    source: ObservableList<T>,
    transform: (T) -> R
) {
    require(this !== source)
    val contentBinding = MappedListContentBinding(this, transform)
    if (this is ObservableList<R>) {
        this.setAll(source.map(transform))
    } else {
        this.clear()
        this.addAll(source.map(transform))
    }
    source.removeListener(contentBinding)
    source.addListener(contentBinding)
}

private class MappedListContentBinding<T, R>(
    list: MutableList<R>?,
    private val transform: (T) -> R
) : ListChangeListener<T>, WeakListener {

    private val listRef: WeakReference<MutableList<R>?> = WeakReference(list)

    override fun onChanged(change: ListChangeListener.Change<out T>) {
        val list = listRef.get()
        if (list == null) {
            change.list.removeListener(this)
        } else {
            while (change.next()) {
                if (change.wasPermutated()) {
                    list.subList(change.from, change.to).clear()
                    list.addAll(
                        change.from,
                        change.list.subList(change.from, change.to)
                            .map(transform)
                    )
                } else {
                    if (change.wasRemoved()) {
                        list.subList(
                            change.from,
                            change.from + change.removedSize
                        ).clear()
                    }
                    if (change.wasAdded()) {
                        list.addAll(
                            change.from,
                            change.addedSubList.map(transform)
                        )
                    }
                }
            }
        }
    }

    override fun wasGarbageCollected(): Boolean = listRef.get() == null

    override fun hashCode(): Int = listRef.get()?.hashCode() ?: 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val list = listRef.get() ?: return false
        return other is MappedListContentBinding<*, *> && list === other.listRef.get()
    }
}

private class MappedSetContentBinding<T, R>(
    set: MutableSet<R>?,
    private val transform: (T) -> R
) : SetChangeListener<T>, WeakListener {

    private val setRef: WeakReference<MutableSet<R>?> = WeakReference(set)

    override fun onChanged(change: SetChangeListener.Change<out T>) {
        val set = setRef.get()
        if (set == null) {
            change.set.removeListener(this)
        } else {
            if (change.wasRemoved()) {
                set.remove(transform(change.elementRemoved))
            } else {
                set.add(transform(change.elementAdded))
            }
        }
    }

    override fun wasGarbageCollected(): Boolean = setRef.get() == null

    override fun hashCode(): Int = setRef.get()?.hashCode() ?: 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val set = setRef.get() ?: return false
        return other is MappedSetContentBinding<*, *> && set === other.setRef.get()
    }
}

fun <T> ObservableList<T>.isNotEmpty(): BooleanBinding =
    Bindings.createBooleanBinding({ (this as List<T>).isNotEmpty() }, this)

operator fun ObservableValue<Boolean>.not(): BooleanBinding =
    Bindings.createBooleanBinding({ !this.value }, this)

fun <T> MutableList<T>.bindContent(source: ObservableList<out T>) =
    Bindings.bindContent(this, source)
