package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.Project
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

object ProjectTable : UUIDTable() {
    val client =
        reference("client", ClientTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 128).uniqueIndex()
    val colorIndex = integer("colorIndex")
    val hourlyRate = decimal("hourlyRate", 128, 32)
    val currency = varchar("currency", 3)
}

class ProjectEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProjectEntity>(ProjectTable)

    var client by ClientEntity referencedOn ProjectTable.client
    var name by ProjectTable.name
    var colorIndex by ProjectTable.colorIndex
    var hourlyRate by ProjectTable.hourlyRate
    var currency by ProjectTable.currency

    fun createModel(): Project = Project(
        id = id.value,
        client = client.createModel(),
        name = name,
        colorIndex = colorIndex,
        hourlyRate = hourlyRate,
        currency = Currency.getInstance(currency)
    )
}
