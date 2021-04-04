package me.mazeika.freelancer.view.admin

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.ClientsAdminBinder
import me.mazeika.freelancer.view.components.EntityForm
import javax.inject.Inject

class ClientsAdminView @Inject constructor(vm: ClientsAdminBinder) :
    EntityAdminView<ClientsAdminBinder.ClientBinder, ClientsAdminBinder.FilledClientBinder>(
        vm
    ) {
    override fun createEntityView(vm: ClientsAdminBinder.ClientBinder): Node =
        EntityForm(
            EntityForm.Text(
                name = "Name",
                value = vm.name,
                maxLength = vm.maxNameLength,
                initialFocus = true
            )
        )
}
