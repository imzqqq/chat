package com.imzqqq.app.flow.components.announcements

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.imzqqq.app.flow.appstore.AnnouncementReadEvent
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.db.InstanceEntity
import com.imzqqq.app.flow.entity.Announcement
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.entity.Instance
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.Either
import com.imzqqq.app.flow.util.Error
import com.imzqqq.app.flow.util.Loading
import com.imzqqq.app.flow.util.Resource
import com.imzqqq.app.flow.util.RxAwareViewModel
import com.imzqqq.app.flow.util.Success
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(
        accountManager: AccountManager,
        private val appDatabase: AppDatabase,
        private val mastodonApi: MastodonApi,
        private val eventHub: EventHub
) : RxAwareViewModel() {

    private val announcementsMutable = MutableLiveData<Resource<List<Announcement>>>()
    val announcements: LiveData<Resource<List<Announcement>>> = announcementsMutable

    private val emojisMutable = MutableLiveData<List<Emoji>>()
    val emojis: LiveData<List<Emoji>> = emojisMutable

    init {
        Single.zip(
            mastodonApi.getCustomEmojis(),
            appDatabase.instanceDao().loadMetadataForInstance(accountManager.activeAccount?.domain!!)
                .map<Either<InstanceEntity, Instance>> { Either.Left(it) }
                .onErrorResumeNext {
                    mastodonApi.getInstance()
                        .map { Either.Right(it) }
                },
            { emojis, either ->
                either.asLeftOrNull()?.copy(emojiList = emojis)
                    ?: InstanceEntity(
                        accountManager.activeAccount?.domain!!,
                        emojis,
                        either.asRight().maxTootChars,
                        either.asRight().pollLimits?.maxOptions,
                        either.asRight().pollLimits?.maxOptionChars,
                        either.asRight().version
                    )
            }
        )
            .doOnSuccess {
                appDatabase.instanceDao().insertOrReplace(it)
            }
            .subscribe(
                {
                    emojisMutable.postValue(it.emojiList.orEmpty())
                },
                {
                    Timber.tag(TAG).w(it, "Failed to get custom emojis.")
                }
            )
            .autoDispose()
    }

    fun load() {
        announcementsMutable.postValue(Loading())
        mastodonApi.listAnnouncements()
            .subscribe(
                {
                    announcementsMutable.postValue(Success(it))
                    it.filter { announcement -> !announcement.read }
                        .forEach { announcement ->
                            mastodonApi.dismissAnnouncement(announcement.id)
                                .subscribe(
                                    {
                                        eventHub.dispatch(AnnouncementReadEvent(announcement.id))
                                    },
                                    { throwable ->
                                        Timber.d(throwable, "Failed to mark announcement as read.")
                                    }
                                )
                                .autoDispose()
                        }
                },
                {
                    announcementsMutable.postValue(Error(cause = it))
                }
            )
            .autoDispose()
    }

    fun addReaction(announcementId: String, name: String) {
        mastodonApi.addAnnouncementReaction(announcementId, name)
            .subscribe(
                {
                    announcementsMutable.postValue(
                        Success(
                            announcements.value!!.data!!.map { announcement ->
                                if (announcement.id == announcementId) {
                                    announcement.copy(
                                        reactions = if (announcement.reactions.find { reaction -> reaction.name == name } != null) {
                                            announcement.reactions.map { reaction ->
                                                if (reaction.name == name) {
                                                    reaction.copy(
                                                        count = reaction.count + 1,
                                                        me = true
                                                    )
                                                } else {
                                                    reaction
                                                }
                                            }
                                        } else {
                                            listOf(
                                                *announcement.reactions.toTypedArray(),
                                                emojis.value!!.find { emoji -> emoji.shortcode == name }
                                                !!.run {
                                                    Announcement.Reaction(
                                                        name,
                                                        1,
                                                        true,
                                                        url,
                                                        staticUrl
                                                    )
                                                }
                                            )
                                        }
                                    )
                                } else {
                                    announcement
                                }
                            }
                        )
                    )
                },
                {
                    Timber.tag(TAG).w(it, "Failed to add reaction to the announcement.")
                }
            )
            .autoDispose()
    }

    fun removeReaction(announcementId: String, name: String) {
        mastodonApi.removeAnnouncementReaction(announcementId, name)
            .subscribe(
                {
                    announcementsMutable.postValue(
                        Success(
                            announcements.value!!.data!!.map { announcement ->
                                if (announcement.id == announcementId) {
                                    announcement.copy(
                                        reactions = announcement.reactions.mapNotNull { reaction ->
                                            if (reaction.name == name) {
                                                if (reaction.count > 1) {
                                                    reaction.copy(
                                                        count = reaction.count - 1,
                                                        me = false
                                                    )
                                                } else {
                                                    null
                                                }
                                            } else {
                                                reaction
                                            }
                                        }
                                    )
                                } else {
                                    announcement
                                }
                            }
                        )
                    )
                },
                {
                    Timber.tag(TAG).w(it, "Failed to remove reaction from the announcement.")
                }
            )
            .autoDispose()
    }

    companion object {
        private const val TAG = "AnnouncementsViewModel"
    }
}
