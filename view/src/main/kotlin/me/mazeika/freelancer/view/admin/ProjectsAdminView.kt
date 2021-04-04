package me.mazeika.freelancer.view.admin

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.ProjectsAdminBinder
import me.mazeika.freelancer.view.components.EntityForm
import javax.inject.Inject

class ProjectsAdminView @Inject constructor(vm: ProjectsAdminBinder) :
    EntityAdminView<ProjectsAdminBinder.ProjectBinder, ProjectsAdminBinder.FilledProjectBinder>(
        vm
    ) {
    override fun createEntityView(vm: ProjectsAdminBinder.ProjectBinder): Node =
        EntityForm(
            EntityForm.Combo(
                name = "Client",
                value = vm.clientName,
                options = vm.clientNames,
                initialFocus = true
            ),
            EntityForm.Text(
                name = "Name",
                value = vm.name,
                maxLength = vm.maxNameLength,
            )
        )
}

