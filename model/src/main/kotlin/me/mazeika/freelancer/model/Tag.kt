package me.mazeika.freelancer.model

class Tag(val name: String) : Comparable<Tag> {
    init {
        require(name.length in 1..32)
    }

    fun isIdentifiedBy(name: String): Boolean =
        this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Tag): Int =
        name.compareTo(other.name, ignoreCase = true)
}
