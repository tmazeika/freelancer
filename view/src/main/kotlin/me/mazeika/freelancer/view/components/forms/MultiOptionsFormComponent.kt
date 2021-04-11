package me.mazeika.freelancer.view.components.forms

import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.layout.FlowPane
import me.mazeika.freelancer.view.components.GraphicCellFactory
import me.mazeika.freelancer.view.util.NoSelectionModel

class MultiOptionsFormComponent<T>(
    override val label: String,
    values: ObservableList<T>,
    options: Collection<T>,
    createCell: (T) -> Node
) : ListView<T>(), FormComponent {
    init {
        isFocusTraversable = false
        selectionModel = NoSelectionModel()
        prefHeight = 100.0
        setCellFactory {
            GraphicCellFactory {
                FlowPane().apply {
                    hgap = 10.0
                    children.setAll(
                        CheckBox().apply {
                            isSelected = it in values
                            selectedProperty().addListener { _, _, isSelected ->
                                if (isSelected && it !in values) {
                                    values.add(it)
                                } else if (!isSelected) {
                                    values.remove(it)
                                }
                            }
                        },
                        createCell(it)
                    )
                }
            }
        }
        items.setAll(options)
    }

    override val node: Node = this
}
