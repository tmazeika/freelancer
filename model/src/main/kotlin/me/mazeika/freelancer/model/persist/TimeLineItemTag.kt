package me.mazeika.freelancer.model.persist

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object TimeLineItemTagTable : Table() {
    val timeLineItem =
        reference(
            "timeLineItem",
            TimeLineItemTable,
            onDelete = ReferenceOption.CASCADE
        )
    val tag = reference("tag", TagTable, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(timeLineItem, tag)
}
