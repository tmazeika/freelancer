package me.mazeika.freelancer.view.components.project

import com.google.inject.assistedinject.Assisted
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import me.mazeika.freelancer.binder.admin.ProjectSnapshot
import javax.inject.Inject

class LongProjectCell @Inject constructor(
    components: ProjectCellComponents,
    @Assisted project: ProjectSnapshot
) : HBox() {
    init {
        alignment = Pos.CENTER_LEFT
        padding = Insets(5.0)
        spacing = 10.0
        children.setAll(
            components.createIndicator(project),
            components.createText(project),
            components.createSpacer(),
            components.createRateText(project)
        )
    }
}
