package org.wordpress.android.ui.pages

import androidx.annotation.IdRes
import androidx.annotation.StringRes
import org.wordpress.android.R
import org.wordpress.android.ui.pages.PageItem.Action.DELETE_PERMANENTLY
import org.wordpress.android.ui.pages.PageItem.Action.MOVE_TO_DRAFT
import org.wordpress.android.ui.pages.PageItem.Action.MOVE_TO_TRASH
import org.wordpress.android.ui.pages.PageItem.Action.PUBLISH_NOW
import org.wordpress.android.ui.pages.PageItem.Action.SET_PARENT
import org.wordpress.android.ui.pages.PageItem.Action.VIEW_PAGE
import org.wordpress.android.ui.pages.PageItem.Type.DIVIDER
import org.wordpress.android.ui.pages.PageItem.Type.EMPTY
import org.wordpress.android.ui.pages.PageItem.Type.PAGE
import java.util.Date

sealed class PageItem(open val type: Type) {
    abstract class Page(
        open val id: Long,
        open val title: String,
        open val date: Date,
        open val labels: List<Int>,
        open var indent: Int,
        open var imageUrl: String?,
        open val actions: Set<Action>,
        open var actionsEnabled: Boolean
    ) : PageItem(PAGE)

    data class PublishedPage(
        override val id: Long,
        override val title: String,
        override val date: Date,
        override val labels: List<Int> = emptyList(),
        override var indent: Int = 0,
        override var imageUrl: String? = null,
        override var actionsEnabled: Boolean = true
    ) : Page(
            id = id,
            title = title,
            date = date,
            labels = labels,
            indent = indent,
            imageUrl = imageUrl,
            actions = setOf(VIEW_PAGE, SET_PARENT, MOVE_TO_DRAFT, MOVE_TO_TRASH),
            actionsEnabled = actionsEnabled
    )

    data class DraftPage(
        override val id: Long,
        override val title: String,
        override val date: Date,
        override val labels: List<Int> = emptyList(),
        override var imageUrl: String? = null,
        override var actionsEnabled: Boolean = true
    ) : Page(
            id = id,
            title = title,
            date = date,
            labels = labels,
            indent = 0,
            imageUrl = imageUrl,
            actions = setOf(VIEW_PAGE, SET_PARENT, PUBLISH_NOW, MOVE_TO_TRASH),
            actionsEnabled = actionsEnabled
    )

    data class ScheduledPage(
        override val id: Long,
        override val title: String,
        override val date: Date,
        override val labels: List<Int> = emptyList(),
        override var imageUrl: String? = null,
        override var actionsEnabled: Boolean = true
    ) : Page(
            id = id,
            title = title,
            date = date,
            labels = labels,
            indent = 0,
            imageUrl = imageUrl,
            actions = setOf(VIEW_PAGE, SET_PARENT, MOVE_TO_DRAFT, MOVE_TO_TRASH),
            actionsEnabled = actionsEnabled
    )

    data class TrashedPage(
        override val id: Long,
        override val title: String,
        override val date: Date,
        override var imageUrl: String? = null,
        override var actionsEnabled: Boolean = true
    ) : Page(
            id = id,
            title = title,
            date = date,
            labels = emptyList(),
            indent = 0,
            imageUrl = imageUrl,
            actions = setOf(VIEW_PAGE, MOVE_TO_DRAFT, DELETE_PERMANENTLY),
            actionsEnabled = actionsEnabled
    )

    data class ParentPage(
        val id: Long,
        val title: String,
        var isSelected: Boolean,
        override val type: Type
    ) : PageItem(type)

    data class Divider(val title: String = "") : PageItem(DIVIDER)

    data class Empty(
        @StringRes val textResource: Int = R.string.empty_list_default,
        val isSearching: Boolean = false,
        val isButtonVisible: Boolean = true,
        val isImageVisible: Boolean = true
    ) : PageItem(EMPTY)

    enum class Type(val viewType: Int) {
        PAGE(1),
        DIVIDER(2),
        EMPTY(3),
        PARENT(4),
        TOP_LEVEL_PARENT(5)
    }

    enum class Action(@IdRes val itemId: Int) {
        VIEW_PAGE(R.id.view_page),
        SET_PARENT(R.id.set_parent),
        PUBLISH_NOW(R.id.publish_now),
        MOVE_TO_DRAFT(R.id.move_to_draft),
        DELETE_PERMANENTLY(R.id.delete_permanently),
        MOVE_TO_TRASH(R.id.move_to_trash);

        companion object {
            fun fromItemId(itemId: Int): Action {
                return values().firstOrNull { it.itemId == itemId }
                        ?: throw IllegalArgumentException("Unexpected item ID in context menu")
            }
        }
    }
}
