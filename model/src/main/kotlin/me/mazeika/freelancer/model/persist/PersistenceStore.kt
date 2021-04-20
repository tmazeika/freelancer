package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Singleton

@Singleton
class PersistenceStore {
    init {
        Database.connect("jdbc:sqlite:data.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                ClientTable,
                ProjectTable,
                TagTable,
                LineItemTagTable,
                LineItemTable
            )
        }
    }

    internal fun loadClients(): List<Client> = transaction {
        ClientEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Client.comparator)
    }

    internal fun addClient(client: Client) = transaction {
        ClientEntity.new {
            name = client.name
            currency = client.currency.currencyCode
        }
    }

    internal fun removeClient(client: Client) = transaction {
        ClientEntity.find { ClientTable.name eq client.name }
            .forEach { it.delete() }
    }

    internal fun loadProjects(): List<Project> = transaction {
        ProjectEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Project.comparator)
    }

    internal fun addProject(project: Project) = transaction {
        ProjectEntity.new {
            client = ClientEntity
                .find { ClientTable.name eq project.client.name }
                .first()
            name = project.name
            colorIndex = project.colorIndex
            hourlyRate = project.hourlyRate
            currency = project.currency.currencyCode
        }
    }

    internal fun removeProject(project: Project) = transaction {
        val query = ProjectTable.innerJoin(ClientTable)
            .slice(ProjectTable.name, ClientTable.name)
            .select {
                (ProjectTable.name eq project.name) and
                        (ClientTable.name eq project.client.name)
            }
            .first()
        ProjectEntity.wrapRow(query).delete()
    }

    internal fun loadTags(): List<Tag> = transaction {
        TagEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(Tag.comparator)
    }

    internal fun addTag(tag: Tag) = transaction {
        TagEntity.new {
            name = tag.name
        }
    }

    internal fun removeTag(tag: Tag) = transaction {
        TagEntity.find { TagTable.name eq tag.name }.forEach { it.delete() }
    }

    internal fun loadLineItems(): List<LineItem> = transaction {
        LineItemEntity.all()
            .mapLazy { it.createModel() }
            .sortedWith(LineItem.comparator)
    }

    internal fun addLineItem(lineItem: TimeLineItem) = transaction {
        LineItemEntity.new {
            project = ProjectEntity.find {
                (ProjectTable.name eq lineItem.project.name) and
                        (ClientTable.name eq lineItem.project.client.name)
            }.first()
            name = lineItem.name
            tags = TagEntity.find {
                TagTable.name inList lineItem.tags.map { it.name }
            }
            start = lineItem.start
            end = lineItem.end
        }
    }

    internal fun removeLineItem(lineItem: LineItem) = transaction {
        LineItemEntity.findById(lineItem.id)?.delete()
    }
}
