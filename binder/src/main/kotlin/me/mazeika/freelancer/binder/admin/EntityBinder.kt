package me.mazeika.freelancer.binder.admin

import javafx.beans.value.ObservableBooleanValue

interface EntityBinder {
    val isValid: ObservableBooleanValue

    override fun toString(): String
}
