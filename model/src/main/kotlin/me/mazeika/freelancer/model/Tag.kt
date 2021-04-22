package me.mazeika.freelancer.model

import java.util.*

data class Tag(val id: UUID, val name: String) : Comparable<Tag> {
    init {
        require(name.length in 1..32)
    }

    fun isIdentifiedBy(name: String): Boolean =
        this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Tag): Int = comparator.compare(this, other)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        return this.compareTo(other as Tag) == 0
    }

    override fun hashCode(): Int = name.toLowerCase().hashCode()

    companion object {
        val comparator: Comparator<Tag> =
            Comparator.comparing(Tag::name, String.CASE_INSENSITIVE_ORDER)
    }
}
