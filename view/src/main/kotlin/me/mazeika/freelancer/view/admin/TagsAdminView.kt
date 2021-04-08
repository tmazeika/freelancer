package me.mazeika.freelancer.view.admin

import javafx.scene.Node
import me.mazeika.freelancer.binder.admin.TagsAdminBinder
import me.mazeika.freelancer.view.components.EntityForm
import javax.inject.Inject

class TagsAdminView @Inject constructor(vm: TagsAdminBinder) :
    EntityAdminView<TagsAdminBinder.TagBinder, TagsAdminBinder.FilledTagBinder>(
        vm
    ) {
    override fun createEntityView(vm: TagsAdminBinder.TagBinder): Node =
        EntityForm(
            EntityForm.TextInput(
                name = "Name",
                value = vm.name,
                maxLength = vm.maxNameLength
            )
        )
}
