package me.mazeika.freelancer.view.admin

import javafx.scene.layout.BorderPane
import me.mazeika.freelancer.binder.admin.TagsAdminBinder
import me.mazeika.freelancer.view.components.forms.GridForm
import me.mazeika.freelancer.view.components.forms.TextFormComponent
import javax.inject.Inject

class TagsAdminView @Inject constructor(vm: TagsAdminBinder) : BorderPane() {
    init {
        top = EntityAdminActionBar(vm) { tag ->
            GridForm(
                TextFormComponent(
                    label = "Name",
                    value = tag.name,
                    maxLength = tag.maxNameLength
                )
            )
        }
        center = EntityAdminList(vm)
    }
}
