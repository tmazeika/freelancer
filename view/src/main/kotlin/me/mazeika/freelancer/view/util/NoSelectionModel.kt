package me.mazeika.freelancer.view.util

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.MultipleSelectionModel

class NoSelectionModel<T> : MultipleSelectionModel<T>() {
    override fun clearAndSelect(index: Int) = Unit

    override fun select(index: Int) = Unit

    override fun select(obj: T) = Unit

    override fun clearSelection(index: Int) = Unit

    override fun clearSelection() = Unit

    override fun isSelected(index: Int): Boolean = false

    override fun isEmpty(): Boolean = true

    override fun selectPrevious() = Unit

    override fun selectNext() = Unit

    override fun selectFirst() = Unit

    override fun selectLast() = Unit

    override fun getSelectedIndices(): ObservableList<Int> =
        FXCollections.emptyObservableList()

    override fun getSelectedItems(): ObservableList<T> =
        FXCollections.emptyObservableList()

    override fun selectIndices(index: Int, vararg indices: Int) = Unit

    override fun selectAll() = Unit
}
