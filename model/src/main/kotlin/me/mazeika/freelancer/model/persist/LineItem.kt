package me.mazeika.freelancer.model.persist

import com.google.common.collect.ImmutableSet
import me.mazeika.freelancer.model.LineItem
import me.mazeika.freelancer.model.TimeLineItem
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.util.*

object LineItemTable : UUIDTable("lineItems") {
    val project =
        reference("project", ProjectTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 128)
    val start = timestamp("start")
    val end = timestamp("end").nullable()
}

class LineItemEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<LineItemEntity>(LineItemTable)

    var project by ProjectEntity referencedOn ProjectTable.client
    var name by LineItemTable.name
    var tags by TagEntity via LineItemTagTable
    var start by LineItemTable.start
    var end by LineItemTable.end

    fun createModel(): LineItem = TimeLineItem(
        id = id.value,
        project = project.createModel(),
        name = name,
        tags = ImmutableSet.copyOf(tags.map { it.createModel() }),
        start = start,
        end = end,
    )
}
