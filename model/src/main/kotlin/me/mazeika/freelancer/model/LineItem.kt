package me.mazeika.freelancer.model

import com.google.common.collect.ImmutableSet
import java.math.BigDecimal
import java.time.Instant
import java.util.*

sealed class LineItem : Comparable<LineItem> {
    abstract val id: UUID
    abstract val project: Project
    abstract val name: String
    abstract val tags: ImmutableSet<Tag>
    abstract val time: Instant

    override fun compareTo(other: LineItem): Int =
        comparator.compare(this, other)

    final override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        return this.compareTo(other as TimeLineItem) == 0
    }

    final override fun hashCode(): Int =
        Objects.hash(time, name.toLowerCase(), project, id)

    companion object {
        val comparator: Comparator<LineItem> =
            Comparator.comparing(LineItem::time)
                .thenComparing(LineItem::name, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(LineItem::id)
                .reversed()
    }
}

data class TimeLineItem(
    override val id: UUID,
    override val project: Project,
    override val name: String,
    override val tags: ImmutableSet<Tag>,
    val start: Instant,
    val end: Instant?
) : LineItem() {
    override val time: Instant = start

    init {
        require(name.length in 1..128)
    }
}

data class SaleLineItem(
    override val id: UUID,
    override val project: Project,
    override val name: String,
    override val tags: ImmutableSet<Tag>,
    override val time: Instant,
    val amount: BigDecimal,
    val currency: Currency
) : LineItem() {

    init {
        require(name.length in 1..128)
        require(amount >= BigDecimal.ZERO)
    }
}
