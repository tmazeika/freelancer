package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.Tag
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object TagTable : UUIDTable() {
    val name = varchar("name", 32).uniqueIndex()
}

class TagEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TagEntity>(TagTable)

    var name by TagTable.name
    val lineItems by TimeLineItemEntity via TimeLineItemTagTable

    fun createModel(): Tag = Tag(
        id = id.value,
        name = name
    )
}
