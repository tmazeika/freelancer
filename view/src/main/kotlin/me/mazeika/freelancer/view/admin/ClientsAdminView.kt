package me.mazeika.freelancer.view.admin

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.ClientsAdminBinder
import me.mazeika.freelancer.binder.i18n.I18nService
import me.mazeika.freelancer.view.components.EntityForm
import javax.inject.Inject

class ClientsAdminView @Inject constructor(vm: ClientsAdminBinder, private val i18nService: I18nService) :
    EntityAdminView<ClientsAdminBinder.ClientBinder, ClientsAdminBinder.FilledClientBinder>(
        vm
    ) {
    override fun createEntityView(vm: ClientsAdminBinder.ClientBinder): Node =
        EntityForm(
            EntityForm.TextInput(
                name = "Name",
                value = vm.name,
                maxLength = vm.maxNameLength
            ),
            EntityForm.ComboInput(
                name = "Currency",
                value = vm.currency,
                options = i18nService.availableCurrencies
            )
        )
}
