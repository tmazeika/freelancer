package me.mazeika.freelancer.binder.admin

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import me.mazeika.freelancer.binder.ListBinder

abstract class AdminBinder<out Binder, Snapshot> : EntityActionBinder<Binder>,
    ListBinder<Snapshot> {

    override val items: ObservableList<Snapshot> =
        FXCollections.observableArrayList()
    final override val selected: ObjectProperty<Snapshot> =
        SimpleObjectProperty()
    final override val isCreateVisible: BooleanProperty =
        SimpleBooleanProperty(true)
    final override val isEditDeleteVisible: BooleanProperty =
        SimpleBooleanProperty()

    init {
        isEditDeleteVisible.bind(selected.isNotNull)
    }
}
