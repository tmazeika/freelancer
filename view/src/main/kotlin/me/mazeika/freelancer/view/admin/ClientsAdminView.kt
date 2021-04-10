package me.mazeika.freelancer.view.admin

import javafx.scene.layout.BorderPane
import me.mazeika.freelancer.binder.admin.ClientsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.components.TextCellFactory
import me.mazeika.freelancer.view.components.forms.GridForm
import me.mazeika.freelancer.view.components.forms.OptionsFormComponent
import me.mazeika.freelancer.view.components.forms.TextFormComponent
import javax.inject.Inject

class ClientsAdminView @Inject constructor(
    vm: ClientsAdminBinder,
    i18nService: I18nService
) : BorderPane() {

    init {
        top = AdminActionBar(vm) { client ->
            GridForm(
                TextFormComponent(
                    label = "Name",
                    value = client.name,
                    maxLength = client.maxNameLength
                ),
                OptionsFormComponent(
                    label = "Currency",
                    value = client.currency,
                    options = i18nService.availableCurrencies
                )
            )
        }
        center = AdminEntityList(vm) { TextCellFactory { it.name.value } }
    }
}
