/* Copyright 2020 Flow Contributors
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

package com.keylesspalace.flow.components.announcements

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.keylesspalace.flow.appstore.AnnouncementReadEvent
import com.keylesspalace.flow.appstore.EventHub
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.db.AppDatabase
import com.keylesspalace.flow.db.InstanceEntity
import com.keylesspalace.flow.entity.Announcement
import com.keylesspalace.flow.entity.Emoji
import com.keylesspalace.flow.entity.Instance
import com.keylesspalace.flow.network.FlowApi
import com.keylesspalace.flow.util.Either
import com.keylesspalace.flow.util.Error
import com.keylesspalace.flow.util.Loading
import com.keylesspalace.flow.util.Resource
import com.keylesspalace.flow.util.RxAwareViewModel
import com.keylesspalace.flow.util.Success
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AnnouncementsViewModel @Inject constructor(
    accountManager: AccountManager,
    private val appDatabase: AppDatabase,
    private val flowApi: FlowApi,
    private val eventHub: EventHub
) : RxAwareViewModel() {

    private val announcementsMutable = MutableLiveData<Resource<List<Announcement>>>()
    val announcements: LiveData<Resource<List<Announcement>>> = announcementsMutable

    private val emojisMutable = MutableLiveData<List<Emoji>>()
    val emojis: LiveData<List<Emoji>> = emojisMutable

    init {
        Single.zip(
            flowApi.getCustomEmojis(),
            appDatabase.instanceDao().loadMetadataForInstance(accountManager.activeAccount?.domain!!)
                .map<Either<InstanceEntity, Instance>> { Either.Left(it) }
                .onErrorResumeNext {
                    flowApi.getInstance()
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
                    Log.w(TAG, "Failed to get custom emojis.", it)
                }
            )
            .autoDispose()
    }

    fun load() {
        announcementsMutable.postValue(Loading())
        flowApi.listAnnouncements()
            .subscribe(
                {
                    announcementsMutable.postValue(Success(it))
                    it.filter { announcement -> !announcement.read }
                        .forEach { announcement ->
                            flowApi.dismissAnnouncement(announcement.id)
                                .subscribe(
                                    {
                                        eventHub.dispatch(AnnouncementReadEvent(announcement.id))
                                    },
                                    { throwable ->
                                        Log.d(TAG, "Failed to mark announcement as read.", throwable)
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
        flowApi.addAnnouncementReaction(announcementId, name)
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
                    Log.w(TAG, "Failed to add reaction to the announcement.", it)
                }
            )
            .autoDispose()
    }

    fun removeReaction(announcementId: String, name: String) {
        flowApi.removeAnnouncementReaction(announcementId, name)
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
                    Log.w(TAG, "Failed to remove reaction from the announcement.", it)
                }
            )
            .autoDispose()
    }

    companion object {
        private const val TAG = "AnnouncementsViewModel"
    }
}
