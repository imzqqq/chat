/* Copyright 2018 charlag
 *
 * This file is a part of Flow.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Flow is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Flow; if not,
 * see <http://www.gnu.org/licenses>. */

package com.keylesspalace.flow.network

import android.util.Log
import com.keylesspalace.flow.appstore.BlockEvent
import com.keylesspalace.flow.appstore.BookmarkEvent
import com.keylesspalace.flow.appstore.EventHub
import com.keylesspalace.flow.appstore.FavoriteEvent
import com.keylesspalace.flow.appstore.MuteConversationEvent
import com.keylesspalace.flow.appstore.MuteEvent
import com.keylesspalace.flow.appstore.PinEvent
import com.keylesspalace.flow.appstore.PollVoteEvent
import com.keylesspalace.flow.appstore.ReblogEvent
import com.keylesspalace.flow.appstore.StatusDeletedEvent
import com.keylesspalace.flow.entity.DeletedStatus
import com.keylesspalace.flow.entity.Poll
import com.keylesspalace.flow.entity.Status
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

/**
 * Created by charlag on 3/24/18.
 */

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
    private val flowApi: FlowApi,
    private val eventHub: EventHub
) : TimelineCases {

    /**
     * Unused yet but can be use for cancellation later. It's always a good idea to save
     * Disposables.
     */
    private val cancelDisposable = CompositeDisposable()

    override fun reblog(statusId: String, reblog: Boolean): Single<Status> {
        val call = if (reblog) {
            flowApi.reblogStatus(statusId)
        } else {
            flowApi.unreblogStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(ReblogEvent(statusId, reblog))
        }
    }

    override fun favourite(statusId: String, favourite: Boolean): Single<Status> {
        val call = if (favourite) {
            flowApi.favouriteStatus(statusId)
        } else {
            flowApi.unfavouriteStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(FavoriteEvent(statusId, favourite))
        }
    }

    override fun bookmark(statusId: String, bookmark: Boolean): Single<Status> {
        val call = if (bookmark) {
            flowApi.bookmarkStatus(statusId)
        } else {
            flowApi.unbookmarkStatus(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(BookmarkEvent(statusId, bookmark))
        }
    }

    override fun muteConversation(statusId: String, mute: Boolean): Single<Status> {
        val call = if (mute) {
            flowApi.muteConversation(statusId)
        } else {
            flowApi.unmuteConversation(statusId)
        }
        return call.doAfterSuccess {
            eventHub.dispatch(MuteConversationEvent(statusId, mute))
        }
    }

    override fun mute(statusId: String, notifications: Boolean, duration: Int?) {
        flowApi.muteAccount(statusId, notifications, duration)
            .subscribe(
                {
                    eventHub.dispatch(MuteEvent(statusId))
                },
                { t ->
                    Log.w("Failed to mute account", t)
                }
            )
            .addTo(cancelDisposable)
    }

    override fun block(statusId: String) {
        flowApi.blockAccount(statusId)
            .subscribe(
                {
                    eventHub.dispatch(BlockEvent(statusId))
                },
                { t ->
                    Log.w("Failed to block account", t)
                }
            )
            .addTo(cancelDisposable)
    }

    override fun delete(statusId: String): Single<DeletedStatus> {
        return flowApi.deleteStatus(statusId)
            .doAfterSuccess {
                eventHub.dispatch(StatusDeletedEvent(statusId))
            }
    }

    override fun pin(statusId: String, pin: Boolean): Single<Status> {
        // Replace with extension method if we use RxKotlin
        return (if (pin) flowApi.pinStatus(statusId) else flowApi.unpinStatus(statusId))
            .doAfterSuccess {
                eventHub.dispatch(PinEvent(statusId, pin))
            }
    }

    override fun voteInPoll(statusId: String, pollId: String, choices: List<Int>): Single<Poll> {
        if (choices.isEmpty()) {
            return Single.error(IllegalStateException())
        }

        return flowApi.voteInPoll(pollId, choices).doAfterSuccess {
            eventHub.dispatch(PollVoteEvent(statusId, it))
        }
    }
}
