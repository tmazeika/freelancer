package me.mazeika.freelancer.model

import java.math.BigDecimal
import java.util.*

data class Project(
    val client: Client,
    val name: String,
    val colorIndex: Int,
    val hourlyRate: BigDecimal,
    val currency: Currency
) : Comparable<Project> {
    init {
        require(name.length in 1..128)
        require(colorIndex >= 0)
        require(hourlyRate >= BigDecimal.ZERO)
    }

    fun isIdentifiedBy(clientName: String, projectName: String): Boolean {
        val clientNameEq = client.name.equals(clientName, ignoreCase = true)
        val nameEq = name.equals(projectName, ignoreCase = true)
        return clientNameEq && nameEq
    }

    override fun compareTo(other: Project): Int =
        Comparator.comparing(Project::client)
            .thenComparing(Project::name, String.CASE_INSENSITIVE_ORDER)
            .compare(this, other)
}
