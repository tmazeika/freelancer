package me.mazeika.freelancer.binder.util

import javafx.beans.WeakListener
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.ObjectBinding
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

//fun <T, U> ObservableValue<T>.select(selectChild: (T) -> ObservableValue<U>): ObjectBinding<U> {
//    var child: ObservableValue<U> = selectChild(this.value)
//    val childBinding = Bindings.createObjectBinding({ child.value })
//    childBinding.invalidate()
//    val invalidateChildBinding = WeakChangeListener<U> { _, _, _ ->
//        childBinding.invalidate()
//    }
//    child.addListener(invalidateChildBinding)
//    this.addListener(WeakChangeListener { _, _, newParent ->
//        child.removeListener(invalidateChildBinding)
//        child = selectChild(newParent).apply {
//            childBinding.invalidate()
//            addListener(invalidateChildBinding)
//        }
//    })
//    return childBinding
//}

fun <T> ObservableList<T>.isNotEmpty(): BooleanBinding =
    Bindings.createBooleanBinding({ (this as List<T>).isNotEmpty() }, this)

operator fun ObservableValue<Boolean>.not(): BooleanBinding =
    Bindings.createBooleanBinding({ !this.value }, this)

fun <T> MutableList<T>.bindContent(source: ObservableList<out T>) =
    Bindings.bindContent(this, source)
