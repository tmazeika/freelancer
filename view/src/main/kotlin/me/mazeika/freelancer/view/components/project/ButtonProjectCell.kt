package me.mazeika.freelancer.view.components.project

import com.google.inject.assistedinject.Assisted
import javafx.scene.layout.FlowPane
import me.mazeika.freelancer.binder.admin.ProjectSnapshot
import javax.inject.Inject

class ButtonProjectCell @Inject constructor(
    components: ProjectCellComponents,
    @Assisted project: ProjectSnapshot
) : FlowPane() {
    init {
        prefHeight = 0.0
        hgap = 10.0
        children.setAll(
            components.createIndicator(project),
            components.createText(project)
        )
    }
}
