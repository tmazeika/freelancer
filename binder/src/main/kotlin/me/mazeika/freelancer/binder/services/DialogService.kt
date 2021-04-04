package me.mazeika.freelancer.binder.services

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node

interface DialogService {
    /**
     * Informs the user of some message with a blocking dialog box.
     */
    fun inform(title: String, message: String)

    /**
     * Warns the user of some message with a blocking dialog box.
     */
    fun warn(title: String, message: String)

    /**
     * Shows the user an error message with a blocking dialog box.
     */
    fun error(title: String, message: String)

    /**
     * Asks the user to confirm something with a blocking dialog box.
     *
     * @return True iff the user clicked OK
     */
    fun confirm(title: String, message: String): Boolean

    /**
     * Prompts the user for some input with a blocking dialog box.
     *
     * @param isValid When false, the OK button is disabled for the user
     * @return True iff the user clicked OK and `onSubmit` last returned true
     */
    fun prompt(
        title: String,
        content: Node,
        isValid: ObservableBooleanValue = SimpleBooleanProperty(true)
    ): Boolean
}
