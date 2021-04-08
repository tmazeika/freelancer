package me.mazeika.freelancer.model

import me.mazeika.freelancer.model.notify.Notifier
import java.util.*
import javax.inject.Singleton
import kotlin.math.max

@Singleton
class Store {
    private val clients: MutableList<Client> = mutableListOf()
    private val projects: MutableList<Project> = mutableListOf()
    private val tags: MutableList<Tag> = mutableListOf()

    val onClientsUpdated: Notifier = Notifier()
    val onProjectsUpdated: Notifier = Notifier()
    val onTagsUpdated: Notifier = Notifier()

    fun getClients(): List<Client> = clients

    fun getProjects(): List<Project> = projects

    fun getTags(): List<Tag> = tags

    fun getClient(name: String): Client =
        clients.first { it.name.equals(name, ignoreCase = true) }

    fun getProject(clientName: String, projectName: String): Project =
        projects.first {
            it.client.name.equals(
                clientName,
                ignoreCase = true
            ) && it.name.equals(projectName, ignoreCase = true)
        }

    fun getTag(name: String): Tag =
        tags.first { it.name.equals(name, ignoreCase = true) }

    fun containsClient(name: String): Boolean =
        clients.any { it.name.equals(name, ignoreCase = true) }

    fun containsProject(clientName: String, projectName: String): Boolean =
        projects.any {
            it.client.name.equals(
                clientName,
                ignoreCase = true
            ) && it.name.equals(projectName, ignoreCase = true)
        }

    fun containsTag(name: String): Boolean =
        tags.any { it.name.equals(name, ignoreCase = true) }

    fun addClient(client: Client) {
        clients.addSorted(client, unique = true)
        onClientsUpdated()
    }

    fun addProject(project: Project) {
        clients.ensureAddedSorted(project.client)
        projects.addSorted(project, unique = true)
        onProjectsUpdated()
    }

    fun addTag(tag: Tag) {
        tags.addSorted(tag, unique = true)
        onTagsUpdated()
    }

    fun replaceClient(old: Client, new: Client) {
        projects.replaceAll {
            if (it.client == old) it.copy(client = new) else it
        }
        clients.removeSorted(old)
        clients.addSorted(new)
        onProjectsUpdated()
        onClientsUpdated()
    }

    fun replaceProject(old: Project, new: Project) {
        projects.removeSorted(old)
        projects.addSorted(new)
        onProjectsUpdated()
    }

    fun replaceTag(old: Tag, new: Tag) {
        tags.removeSorted(old)
        tags.addSorted(new)
        onTagsUpdated()
    }

    fun removeClient(client: Client) {
        projects.removeAll { it.client == client }
        clients.removeSorted(client)
        onProjectsUpdated()
        onClientsUpdated()
    }

    fun removeProject(project: Project) {
        projects.removeSorted(project)
        onProjectsUpdated()
    }

    fun removeTag(tag: Tag) {
        tags.removeSorted(tag)
        onTagsUpdated()
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
