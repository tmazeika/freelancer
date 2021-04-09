package me.mazeika.freelancer.binder.admin

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

abstract class AdminBinder<out EB, FilledEB> : EntityActionHandler<EB>,
    EntityAdmin<FilledEB> {

    override val entities: ObservableList<FilledEB> =
        FXCollections.observableArrayList()
    final override val selected: ObjectProperty<FilledEB> =
        SimpleObjectProperty()
    final override val isEditDeleteVisible: BooleanProperty =
        SimpleBooleanProperty()

    init {
        isEditDeleteVisible.bind(selected.isNotNull)
    }
}
