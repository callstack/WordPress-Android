package org.wordpress.android.ui.posts

import android.text.TextUtils
import org.wordpress.android.R
import org.wordpress.android.fluxc.model.PostModel
import org.wordpress.android.fluxc.model.post.PostStatus
import org.wordpress.android.ui.stats.refresh.utils.DateUtils
import org.wordpress.android.viewmodel.ResourceProvider
import javax.inject.Inject

class PostSettingsUtils
@Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val dateUtils: DateUtils
) {
    fun getPublishDateLabel(
        postModel: PostModel
    ): String {
        val labelToUse: String
        val dateCreated = postModel.dateCreated
        if (!TextUtils.isEmpty(dateCreated)) {
            val formattedDate = dateUtils.formatDateTime(dateCreated)

            val status = PostStatus.fromPost(postModel)
            if (status == PostStatus.SCHEDULED) {
                labelToUse = resourceProvider.getString(R.string.scheduled_for, formattedDate)
            } else if (status == PostStatus.PUBLISHED || status == PostStatus.PRIVATE) {
                labelToUse = resourceProvider.getString(R.string.published_on, formattedDate)
            } else if (postModel.isLocalDraft) {
                if (PostUtils.isPublishDateInThePast(postModel)) {
                    labelToUse = resourceProvider.getString(R.string.backdated_for, formattedDate)
                } else if (PostUtils.shouldPublishImmediately(postModel)) {
                    labelToUse = resourceProvider.getString(R.string.immediately)
                } else {
                    labelToUse = resourceProvider.getString(R.string.publish_on, formattedDate)
                }
            } else if (PostUtils.isPublishDateInTheFuture(postModel)) {
                labelToUse = resourceProvider.getString(R.string.schedule_for, formattedDate)
            } else {
                labelToUse = resourceProvider.getString(R.string.publish_on, formattedDate)
            }
        } else if (PostUtils.shouldPublishImmediatelyOptionBeAvailable(postModel)) {
            labelToUse = resourceProvider.getString(R.string.immediately)
        } else {
            // TODO: What should the label be if there is no specific date and this is not a DRAFT?
            labelToUse = ""
        }
        return labelToUse
    }
}
