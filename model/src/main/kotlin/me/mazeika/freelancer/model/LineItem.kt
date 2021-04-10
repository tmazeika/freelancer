package me.mazeika.freelancer.model

import java.math.BigDecimal
import java.time.Instant
import java.util.*

sealed class LineItem(
    val id: UUID,
    val name: String,
    val project: Project,
    tags: List<Tag>,
    val time: Instant
) : Comparable<LineItem> {

    val tags: List<Tag> = tags.toList()

    init {
        require(name == name.trim() && name.length in 1..128)
    }

    override fun compareTo(other: LineItem): Int =
        Comparator.comparing(LineItem::time)
            .thenComparing(LineItem::name)
            .thenComparing(LineItem::project)
            .thenComparing(LineItem::id)
            .compare(this, other)
}

class TimeLineItem(
    id: UUID,
    name: String,
    project: Project,
    tags: List<Tag>,
    start: Instant,
    val end: Instant?
) : LineItem(id, name, project, tags, start)

class SaleLineItem(
    id: UUID,
    name: String,
    project: Project,
    tags: List<Tag>,
    time: Instant,
    val amount: BigDecimal,
    val currency: Currency
) : LineItem(id, name, project, tags, time) {

    init {
        require(amount >= BigDecimal.ZERO)
    }
}
