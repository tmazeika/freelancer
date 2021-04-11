package me.mazeika.freelancer.view.components.project

import com.google.inject.assistedinject.Assisted
import javafx.scene.layout.FlowPane
import me.mazeika.freelancer.binder.admin.ProjectSnapshot
import javax.inject.Inject

class ShortProjectCell @Inject constructor(
    components: ProjectCellComponents,
    @Assisted project: ProjectSnapshot
) : FlowPane() {
    init {
        hgap = 10.0
        children.setAll(
            components.createIndicator(project),
            components.createText(project)
        )
    }
}
