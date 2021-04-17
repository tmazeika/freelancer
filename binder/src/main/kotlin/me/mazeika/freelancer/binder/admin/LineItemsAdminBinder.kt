package me.mazeika.freelancer.binder.admin

import com.google.common.collect.ImmutableSet
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import me.mazeika.freelancer.binder.services.DialogService
import me.mazeika.freelancer.binder.util.bindContent
import me.mazeika.freelancer.binder.util.isNotEmpty
import me.mazeika.freelancer.model.Store
import me.mazeika.freelancer.model.TimeLineItem
import me.mazeika.freelancer.model.util.map
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.inject.Inject

class LineItemsAdminBinder @Inject constructor(
    private val store: Store,
    private val dialogService: DialogService
) : AdminBinder<TimeLineItemBinder, TimeLineItemSnapshot>() {

    val allProjects: ObservableList<ProjectSnapshot> =
        FXCollections.observableArrayList()
    val allTags: ObservableList<TagSnapshot> =
        FXCollections.observableArrayList()

    init {
        allProjects.bindContent(store.projects, ::ProjectSnapshot)
        allTags.bindContent(store.tags, ::TagSnapshot)
        items.bindContent(store.lineItems) {
            TimeLineItemSnapshot(it as TimeLineItem)
        }
        isCreateVisible.bind(store.projects.isNotEmpty())
    }

    fun onResume(item: TimeLineItemSnapshot) {
        store.addLineItem(
            item.timeLineItem.copy(
                id = UUID.randomUUID(),
                start = Instant.now().truncatedTo(ChronoUnit.SECONDS),
                end = null
            )
        )
    }

    fun onStop(item: TimeLineItemSnapshot) {
        store.replaceLineItem(
            old = item.timeLineItem,
            new = item.timeLineItem.copy(end = Instant.now().let {
                if (it.nano > 0) {
                    it.plusSeconds(1).truncatedTo(ChronoUnit.SECONDS)
                } else {
                    it
                }
            })
        )
    }

    override fun onCreate(dialogViewFactory: (TimeLineItemBinder) -> Node): Boolean {
        val binder = EmptyTimeLineItemBinder()
        val ok = dialogService.prompt(
            title = "Create Time Line Item",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.addLineItem(binder.createTimeLineItem())
        }
        return ok
    }

    override fun onEdit(dialogViewFactory: (TimeLineItemBinder) -> Node): Boolean {
        val binder = FilledTimeLineItemBinder(selected.value.timeLineItem)
        val ok = dialogService.prompt(
            title = "Edit Time Line Item",
            content = dialogViewFactory(binder),
            isValid = binder.isValid
        )
        if (ok) {
            store.replaceLineItem(
                old = binder.timeLineItem,
                new = binder.createTimeLineItem()
            )
        }
        return ok
    }

    override fun onDelete(): Boolean {
        val binder = FilledTimeLineItemBinder(selected.value.timeLineItem)
        val ok = dialogService.confirm(
            title = "Delete Time Line Item",
            message = "Are you sure you want to delete \"${binder.name.value}\"?"
        )
        if (ok) {
            store.removeLineItem(binder.timeLineItem)
        }
        return ok
    }

    private inner class EmptyTimeLineItemBinder : TimeLineItemBinder(
        id = UUID.randomUUID(),
        name = "",
        project = allProjects[0],
        tags = ImmutableSet.of(),
        start = Instant.now(),
        end = null,
    )

    private inner class FilledTimeLineItemBinder(val timeLineItem: TimeLineItem) :
        TimeLineItemBinder(
            id = timeLineItem.id,
            name = timeLineItem.name,
            project = ProjectSnapshot(timeLineItem.project),
            tags = timeLineItem.tags.map(::TagSnapshot),
            start = timeLineItem.start,
            end = timeLineItem.end
        )
}
