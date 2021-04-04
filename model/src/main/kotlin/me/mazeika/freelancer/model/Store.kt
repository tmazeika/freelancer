package me.mazeika.freelancer.model

import java.util.*
import kotlin.math.max

class Store {
    private val clients: MutableList<Client> = mutableListOf()
    private val projects: MutableList<Project> = mutableListOf()
    private val tags: MutableList<Tag> = mutableListOf()

    fun getClients(): List<Client> = clients

    fun getProjects(): List<Project> = projects

    fun getTags(): List<Tag> = tags

    fun containsTag(name: String): Boolean =
        tags.any { it.name.equals(name, ignoreCase = true) }

    fun addClient(client: Client) {
        clients.addSorted(client, unique = true)
    }

    fun addProject(project: Project) {
        clients.ensureAddedSorted(project.client)
        projects.addSorted(project, unique = true)
    }

    fun addTag(tag: Tag) {
        tags.addSorted(tag, unique = true)
    }

    fun removeProject(project: Project) {
        projects.removeSorted(project)
    }

    fun removeTag(tag: Tag) {
        tags.removeSorted(tag)
    }

    private fun <T> MutableList<T>.addSorted(
        e: T,
        unique: Boolean = false
    ) where T : Comparable<T> {
        val i = Collections.binarySearch(this, e)
        require(!unique || i < 0)
        this.add(max(-i - 1, 0), e)
    }

    private fun <T> MutableList<T>.ensureAddedSorted(e: T) where T : Comparable<T> {
        val i = Collections.binarySearch(this, e)
        if (i < 0) {
            this.add(max(-i - 1, 0), e)
        }
    }

    private fun <T> MutableList<T>.removeSorted(e: T) where T : Comparable<T> {
        val i = Collections.binarySearch(this, e)
        require(i >= 0)
        this.removeAt(i)
    }
}
