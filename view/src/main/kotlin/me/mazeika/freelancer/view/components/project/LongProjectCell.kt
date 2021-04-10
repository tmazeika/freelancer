package me.mazeika.freelancer.view.components.project

import com.google.inject.assistedinject.Assisted
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import me.mazeika.freelancer.binder.admin.ProjectBinder
import javax.inject.Inject

class LongProjectCell @Inject constructor(
    components: ProjectCellComponents,
    @Assisted project: ProjectBinder
) : HBox() {
    init {
        alignment = Pos.CENTER_LEFT
        spacing = 10.0
        children.setAll(
            components.createIndicator(project),
            components.createText(project),
            components.createSpacer(),
            components.createRateText(project)
        )
    }
}
