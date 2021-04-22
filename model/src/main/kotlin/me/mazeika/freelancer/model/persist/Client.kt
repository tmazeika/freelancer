package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.Client
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object ClientTable : UUIDTable() {
    val name = varchar("name", 128).uniqueIndex()
    val currency = varchar("currency", 3)
}

class ClientEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ClientEntity>(ClientTable)

    var name by ClientTable.name
    var currency by ClientTable.currency
    val projects by ProjectEntity referrersOn ProjectTable.client

    fun createModel(): Client = Client(
        id = id.value,
        name = name,
        currency = Currency.getInstance(currency)
    )
}
