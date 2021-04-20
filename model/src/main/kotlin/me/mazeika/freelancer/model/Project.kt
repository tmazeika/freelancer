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
        require(hourlyRate.precision() <= 128)
        require(hourlyRate.scale() <= 32)
    }

    fun isIdentifiedBy(clientName: String, name: String): Boolean =
        this.client.name.equals(clientName, ignoreCase = true)
                && this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Project): Int =
        comparator.compare(this, other)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Project
        return client == other.client
                && name.equals(other.name, ignoreCase = true)
    }

    override fun hashCode(): Int = Objects.hash(client, name.toLowerCase())

    companion object {
        val comparator: Comparator<Project> =
            Comparator.comparing(Project::client)
                .thenComparing(Project::name, String.CASE_INSENSITIVE_ORDER)
    }
}
