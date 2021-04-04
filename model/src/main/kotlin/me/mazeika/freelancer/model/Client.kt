package me.mazeika.freelancer.model

data class Client(val name: String) : Comparable<Client> {
    init {
        require(name.length in 1..255)
    }

    override fun compareTo(other: Client): Int =
        name.compareTo(other.name, ignoreCase = true)
}
