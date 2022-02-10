package com.imzqqq.app.flow.network

import android.util.Log
import com.imzqqq.app.flow.appstore.BlockEvent
import com.imzqqq.app.flow.appstore.BookmarkEvent
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.FavoriteEvent
import com.imzqqq.app.flow.appstore.MuteConversationEvent
import com.imzqqq.app.flow.appstore.MuteEvent
import com.imzqqq.app.flow.appstore.PinEvent
import com.imzqqq.app.flow.appstore.PollVoteEvent
import com.imzqqq.app.flow.appstore.ReblogEvent
import com.imzqqq.app.flow.appstore.StatusDeletedEvent
import com.imzqqq.app.flow.entity.DeletedStatus
import com.imzqqq.app.flow.entity.Poll
import com.imzqqq.app.flow.entity.Status
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber

interface TimelineCases {
    fun reblog(statusId: String, reblog: Boolean): Single<Status>
    fun favourite(statusId: String, favourite: Boolean): Single<Status>
    fun bookmark(statusId: String, bookmark: Boolean): Single<Status>
    fun mute(statusId: String, notifications: Boolean, duration: Int?)
    fun block(statusId: String)
    fun delete(statusId: String): Single<DeletedStatus>
    fun pin(statusId: String, pin: Boolean): Single<Status>
    fun voteInPoll(statusId: String, pollId: String, choices: List<Int>): Single<Poll>
    fun muteConversation(statusId: String, mute: Boolean): Single<Status>
}

class TimelineCasesImpl(
        private val mastodonApi: MastodonApi,
        private val eventHub: EventHub
) : TimelineCases {

    /**
     * Unused yet but can be use for cancellation later. It's always a good idea to save
     * Disposables.
     */
    private val cancelDisposable = CompositeDisposable()

    override fun reblog(statusId: String, reblog: Boolean): Single<Status> {
        val call = if (reblog) {
            mastodonApi.reblogStatus(statusId)
        } else {
            mastodonApi.unreblogStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(ReblogEvent(statusId, reblog))
        }
    }

    override fun favourite(statusId: String, favourite: Boolean): Single<Status> {
        val call = if (favourite) {
            mastodonApi.favouriteStatus(statusId)
        } else {
            mastodonApi.unfavouriteStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(FavoriteEvent(statusId, favourite))
        }
    }

    override fun bookmark(statusId: String, bookmark: Boolean): Single<Status> {
        val call = if (bookmark) {
            mastodonApi.bookmarkStatus(statusId)
        } else {
            mastodonApi.unbookmarkStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(BookmarkEvent(statusId, bookmark))
        }
    }

    override fun muteConversation(statusId: String, mute: Boolean): Single<Status> {
        val call = if (mute) {
            mastodonApi.muteConversation(statusId)
        } else {
            mastodonApi.unmuteConversation(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(MuteConversationEvent(statusId, mute))
        }
    }

    override fun mute(statusId: String, notifications: Boolean, duration: Int?) {
        mastodonApi.muteAccount(statusId, notifications, duration)
            .subscribe(
                {
                    eventHub.dispatch(MuteEvent(statusId))
                },
                { t ->
                    Timber.tag("Failed to mute account").w(t)
                }
            )
            .addTo(cancelDisposable)
    }

    override fun block(statusId: String) {
        mastodonApi.blockAccount(statusId)
            .subscribe(
                {
                    eventHub.dispatch(BlockEvent(statusId))
                },
                { t ->
                    Timber.tag("Failed to block account").w(t)
                }
            )
            .addTo(cancelDisposable)
    }

    override fun delete(statusId: String): Single<DeletedStatus> {
        return mastodonApi.deleteStatus(statusId)
            .doAfterSuccess {
                eventHub.dispatch(StatusDeletedEvent(statusId))
            }
    }

    override fun pin(statusId: String, pin: Boolean): Single<Status> {
        // Replace with extension method if we use RxKotlin
        return (if (pin) mastodonApi.pinStatus(statusId) else mastodonApi.unpinStatus(statusId))
            .doAfterSuccess {
                eventHub.dispatch(PinEvent(statusId, pin))
            }
    }

    override fun voteInPoll(statusId: String, pollId: String, choices: List<Int>): Single<Poll> {
        if (choices.isEmpty()) {
            return Single.error(IllegalStateException())
        }

        return mastodonApi.voteInPoll(pollId, choices).doAfterSuccess {
            eventHub.dispatch(PollVoteEvent(statusId, it))
        }
    }
}
