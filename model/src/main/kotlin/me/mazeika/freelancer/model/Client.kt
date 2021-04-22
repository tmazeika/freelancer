package me.mazeika.freelancer.model

import java.util.*

data class Client(val id: UUID, val name: String, val currency: Currency) :
    Comparable<Client> {
    init {
        require(name.length in 1..128)
    }

    fun isIdentifiedBy(name: String): Boolean =
        this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Client): Int =
        comparator.compare(this, other)

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false
        return this.compareTo(other as Client) == 0
    }

    override fun hashCode(): Int = name.toLowerCase().hashCode()

    companion object {
        val comparator: Comparator<Client> =
            Comparator.comparing(Client::name, String.CASE_INSENSITIVE_ORDER)
    }
}
