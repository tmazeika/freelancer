package me.mazeika.freelancer.model.util

import com.google.common.collect.ImmutableSet
import java.util.*
import kotlin.math.max

fun <T> MutableList<T>.addSorted(obj: T) where T : Comparable<T> {
    val i = Collections.binarySearch(this, obj)
    require(i < 0)
    this.add(max(-i - 1, 0), obj)
}

fun <T> MutableList<T>.removeSorted(obj: T) where T : Comparable<T> {
    val i = Collections.binarySearch(this, obj)
    require(i >= 0)
    this.removeAt(i)
}

fun <T> MutableList<T>.replaceSorted(old: T, new: T) where T : Comparable<T> {
    this.removeSorted(old)
    this.addSorted(new)
}

fun <T> MutableList<T>.replaceAllSorted(
    predicate: (T) -> Boolean,
    transform: (T) -> T
) where T : Comparable<T> {
    val toAdd = mutableListOf<T>()
    this.removeIf { item ->
        predicate(item).also { if (it) toAdd += transform(item) }
    }
    toAdd.forEach(this::addSorted)
}

fun <T, R> ImmutableSet<T>.map(transform: (T) -> R): ImmutableSet<R> =
    ImmutableSet.copyOf((this as Set<T>).map(transform))

fun <T> ImmutableSet<T>.replaced(old: T, new: T): ImmutableSet<T> =
    ImmutableSet.copyOf(this.filter { it != old } + new)

fun <T> ImmutableSet<T>.without(obj: T): ImmutableSet<T> =
    ImmutableSet.copyOf(this.filter { it != obj })
