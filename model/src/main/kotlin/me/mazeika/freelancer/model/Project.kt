package me.mazeika.freelancer.model

import java.util.*

data class Project(
    val client: Client,
    val name: String,
    val colorIndex: Int,
    val hourlyCost: Long,
    val currency: Currency
) : Comparable<Project> {
    init {
        require(name.length in 1..255)
        require(colorIndex >= 0)
        require(hourlyCost >= 0)
    }

    override fun compareTo(other: Project): Int =
        Comparator.comparing(Project::client)
            .thenComparing(Project::name, String.CASE_INSENSITIVE_ORDER)
            .compare(this, other)
}
