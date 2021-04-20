package me.mazeika.freelancer.model.persist

import me.mazeika.freelancer.model.Client
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import java.util.*

object ClientTable : IntIdTable("clients") {
    val name = varchar("name", 128).uniqueIndex()
    val currency = varchar("currency", 3)
}

class ClientEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClientEntity>(ClientTable)

    var name by ClientTable.name
    var currency by ClientTable.currency
    val projects by ProjectEntity referrersOn ProjectTable.client

    fun createModel(): Client = Client(
        name = name,
        currency = Currency.getInstance(currency)
    )
}
