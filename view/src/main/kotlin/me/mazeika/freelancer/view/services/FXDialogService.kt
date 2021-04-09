package me.mazeika.freelancer.view.services

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableBooleanValue
import javafx.scene.Node
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import me.mazeika.freelancer.binder.services.DialogService

class FXDialogService : DialogService {

    override fun inform(title: String, message: String) {
        showSimpleAlert(Alert.AlertType.INFORMATION, title, message)
    }

    override fun warn(title: String, message: String) {
        showSimpleAlert(Alert.AlertType.WARNING, title, message)
    }

    override fun error(title: String, message: String) {
        showSimpleAlert(Alert.AlertType.ERROR, title, message)
    }

    override fun confirm(title: String, message: String): Boolean =
        showSimpleAlert(
            Alert.AlertType.CONFIRMATION,
            title,
            message,
            ButtonType.CANCEL,
            ButtonType.YES
        ).map { it == ButtonType.YES }.orElse(false)

    override fun prompt(
        title: String,
        content: Node,
        isValid: ObservableBooleanValue
    ): Boolean =
        Alert(Alert.AlertType.NONE).let {
            it.title = title
            it.dialogPane.content = content
            it.buttonTypes.setAll(ButtonType.CANCEL, ButtonType.OK)
            (it.dialogPane.lookupButton(ButtonType.OK) as Button).apply {
                disableProperty().bind(Bindings.not(isValid))
            }
            it.showAndWait()
        }.map { it == ButtonType.OK }.orElse(false)

    private fun showSimpleAlert(
        type: Alert.AlertType,
        title: String,
        message: String,
        vararg buttonTypes: ButtonType
    ) = Alert(type, message).let {
        it.title = title
        it.headerText = null
        if (buttonTypes.isNotEmpty()) {
            it.buttonTypes.setAll(*buttonTypes)
        }
        it.showAndWait()
    }
}
