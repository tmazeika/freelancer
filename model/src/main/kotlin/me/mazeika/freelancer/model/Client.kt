package me.mazeika.freelancer.model

data class Client(val name: String) : Comparable<Client> {
    init {
        require(name.length in 1..128)
    }

    fun isIdentifiedBy(name: String): Boolean =
        this.name.equals(name, ignoreCase = true)

    override fun compareTo(other: Client): Int =
        name.compareTo(other.name, ignoreCase = true)
}
