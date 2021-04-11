package me.mazeika.freelancer.model

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import me.mazeika.freelancer.model.util.*
import javax.inject.Singleton

@Singleton
class Store {
    private val _clients: ObservableList<Client> =
        FXCollections.observableArrayList()
    val clients: ObservableList<Client> =
        FXCollections.unmodifiableObservableList(_clients)

    private val _projects: ObservableList<Project> =
        FXCollections.observableArrayList()
    val projects: ObservableList<Project> =
        FXCollections.unmodifiableObservableList(_projects)

    private val _tags: ObservableList<Tag> = FXCollections.observableArrayList()
    val tags: ObservableList<Tag> =
        FXCollections.unmodifiableObservableList(_tags)

    private val _lineItems: ObservableList<LineItem> =
        FXCollections.observableArrayList()
    val lineItems: ObservableList<LineItem> =
        FXCollections.unmodifiableObservableList(_lineItems)

    fun containsClient(name: String): Boolean =
        _clients.any { it.isIdentifiedBy(name) }

    fun containsProject(clientName: String, projectName: String): Boolean =
        _projects.any { it.isIdentifiedBy(clientName, projectName) }

    fun containsTag(name: String): Boolean =
        _tags.any { it.isIdentifiedBy(name) }

    fun addClient(client: Client) {
        _clients.addSorted(client)
    }

    fun addProject(project: Project) {
        _projects.addSorted(project)
    }

    fun addTag(tag: Tag) {
        _tags.addSorted(tag)
    }

    fun addLineItem(lineItem: LineItem) {
        _lineItems.addSorted(lineItem)
    }

    fun replaceClient(old: Client, new: Client) {
        _clients.replaceSorted(old, new)
        _projects.filter { it.client == old }.forEach {
            replaceProject(it, it.copy(client = new))
        }
    }

    fun replaceProject(old: Project, new: Project) {
        _projects.replaceSorted(old, new)
        _lineItems.replaceAllSorted({ it.project == old }, { lineItem ->
            when (lineItem) {
                is TimeLineItem -> lineItem.copy(project = new)
                is SaleLineItem -> lineItem.copy(project = new)
            }
        })
    }

    fun replaceTag(old: Tag, new: Tag) {
        _tags.replaceSorted(old, new)
        _lineItems.replaceAllSorted({ old in it.tags }, { lineItem ->
            val newTags = lineItem.tags.replaced(old, new)
            when (lineItem) {
                is TimeLineItem -> lineItem.copy(tags = newTags)
                is SaleLineItem -> lineItem.copy(tags = newTags)
            }
        })
    }

    fun replaceLineItem(old: LineItem, new: LineItem) {
        _lineItems.replaceSorted(old, new)
    }

    fun removeClient(client: Client) {
        _clients.removeSorted(client)
        _projects.removeIf { it.client == client }
        _lineItems.removeIf { it.project.client == client }
    }

    fun removeProject(project: Project) {
        _projects.removeSorted(project)
        _lineItems.removeIf { it.project == project }
    }

    fun removeTag(tag: Tag) {
        _tags.removeSorted(tag)
        _lineItems.replaceAllSorted({ tag in it.tags }, { lineItem ->
            val newTags = lineItem.tags.without(tag)
            when (lineItem) {
                is TimeLineItem -> lineItem.copy(tags = newTags)
                is SaleLineItem -> lineItem.copy(tags = newTags)
            }
        })
    }

    fun removeLineItem(lineItem: LineItem) {
        _lineItems.removeSorted(lineItem)
    }
}
