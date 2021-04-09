package me.mazeika.freelancer.binder.admin

import javafx.beans.property.BooleanProperty
import javafx.scene.Node

interface EntityActionBinder<out T> {

    val isEditDeleteVisible: BooleanProperty

    fun onCreate(dialogViewFactory: (T) -> Node): Boolean

    fun onEdit(dialogViewFactory: (T) -> Node): Boolean

    fun onDelete(): Boolean
}
