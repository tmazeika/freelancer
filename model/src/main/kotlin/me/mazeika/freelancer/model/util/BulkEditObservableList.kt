package me.mazeika.freelancer.model.util

import javafx.collections.ModifiableObservableListBase

class BulkEditObservableList<T> : ModifiableObservableListBase<T>() {
    private val list: MutableList<T> = mutableListOf()

    override fun get(index: Int): T = list[index]

    override fun doAdd(index: Int, element: T) = list.add(index, element)

    override fun doSet(index: Int, element: T): T = list.set(index, element)

    override fun doRemove(index: Int): T = list.removeAt(index)

    override val size: Int
        get() = list.size

    companion object {
        fun runChanges(
            vararg lists: BulkEditObservableList<*>,
            block: () -> Unit
        ) {
            lists.forEach { it.beginChange() }
            try {
                block()
            } finally {
                lists.forEach { it.endChange() }
            }
        }
    }
}
