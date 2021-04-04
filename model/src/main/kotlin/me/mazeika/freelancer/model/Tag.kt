package me.mazeika.freelancer.model

data class Tag(val name: String) : Comparable<Tag> {
    init {
        require(name.length in 1..255)
    }

    override fun compareTo(other: Tag): Int =
        name.compareTo(other.name, ignoreCase = true)
}
