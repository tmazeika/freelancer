package me.mazeika.freelancer.model.persist

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import me.mazeika.freelancer.model.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.mapLazy
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceStore @Inject constructor(private val scope: CoroutineScope) {
    private val context = newSingleThreadContext("PersistenceStore")

    init {
        Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                ClientTable,
                ProjectTable,
                TagTable,
                TimeLineItemTagTable,
                TimeLineItemTable
            )
        }
    }

    internal fun loadClients(): List<Client> =
        ClientEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Client.comparator)

    internal fun addClient(client: Client) {
        ClientEntity.new(client.id) {
            name = client.name
            currency = client.currency.currencyCode
        }
    }

    internal fun removeClient(client: Client) {
        ClientEntity[client.id].delete()
    }

    internal fun loadProjects(): List<Project> =
        ProjectEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Project.comparator)

    internal fun addProject(project: Project) {
        ProjectEntity.new(project.id) {
            client = ClientEntity[project.client.id]
            name = project.name
            colorIndex = project.colorIndex
            hourlyRate = project.hourlyRate
            currency = project.currency.currencyCode
        }
    }

    internal fun removeProject(project: Project) {
        ProjectEntity[project.id].delete()
    }

    internal fun loadTags(): List<Tag> =
        TagEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Tag.comparator)

    internal fun addTag(tag: Tag) {
        TagEntity.new(tag.id) {
            name = tag.name
        }
    }

    internal fun removeTag(tag: Tag) {
        TagEntity[tag.id].delete()
    }

    internal fun loadTimeLineItems(): List<TimeLineItem> =
        TimeLineItemEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(LineItem.comparator)

    internal fun addTimeLineItem(timeLineItem: TimeLineItem) {
        TimeLineItemEntity.new(timeLineItem.id) {
            project = ProjectEntity[timeLineItem.project.id]
            name = timeLineItem.name
            start = timeLineItem.start
            end = timeLineItem.end
        }.apply {
            tags =
                TagEntity.find { TagTable.id inList timeLineItem.tags.map { it.id } }
        }
    }

    internal fun removeTimeLineItem(timeLineItem: TimeLineItem) {
        TimeLineItemEntity[timeLineItem.id].delete()
    }

    internal fun <T> syncObservableList(
        source: ObservableList<T>,
        onAdd: (T) -> Unit,
        onRemove: (T) -> Unit
    ) {
        source.addListener(ListChangeListener { change ->
            scope.launch(context) {
                transaction {
                    while (change.next()) {
                        change.removed.forEach { onRemove(it) }
                        change.addedSubList.forEach { onAdd(it) }
                    }
                }
            }
        })
    }
}
