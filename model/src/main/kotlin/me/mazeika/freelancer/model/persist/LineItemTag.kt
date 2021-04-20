package me.mazeika.freelancer.model.persist

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object LineItemTagTable : Table("lineItemTags") {
    val lineItem =
        reference("lineItem", LineItemTable, onDelete = ReferenceOption.CASCADE)
    val tag = reference("tag", TagTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(lineItem, tag)
}
