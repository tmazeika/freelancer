package me.mazeika.freelancer.model

import java.util.*

data class Client(val name: String, val currency: Currency) :
    Comparable<Client> {
    init {
        require(name.length in 1..128)
    }

    fun isIdentifiedBy(name: String): Boolean =
        this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Client): Int =
        name.compareTo(other.name, ignoreCase = true)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Client
        return name.equals(other.name, ignoreCase = true)
    }

    override fun hashCode(): Int = name.toLowerCase().hashCode()
}
