package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.Tag
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TagTable : IntIdTable("tags") {
    val name = varchar("name", 32).uniqueIndex()
}

class TagEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TagEntity>(TagTable)

    var name by TagTable.name
    val lineItems by LineItemEntity via LineItemTagTable

    fun createModel(): Tag = Tag(
        name = name
    )
}
