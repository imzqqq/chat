package com.imzqqq.app.flow.components.conversation

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.network.TimelineCases
import com.imzqqq.app.flow.util.RxAwareViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await
import timber.log.Timber
import javax.inject.Inject

class ConversationsViewModel @Inject constructor(
    private val timelineCases: TimelineCases,
    private val database: AppDatabase,
    private val accountManager: AccountManager,
    private val api: MastodonApi
) : RxAwareViewModel() {

@OptIn(ExperimentalPagingApi::class)
val conversationFlow = Pager(
        config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 20),
        remoteMediator = ConversationsRemoteMediator(accountManager.activeAccount!!.id, api, database),
        pagingSourceFactory = { database.conversationDao().conversationsForAccount(accountManager.activeAccount!!.id) }
    )
        .flow
        .cachedIn(viewModelScope)

    fun favourite(favourite: Boolean, conversation: ConversationEntity) {
        viewModelScope.launch {
            try {
                timelineCases.favourite(conversation.lastStatus.id, favourite).await()

                val newConversation = conversation.copy(
                    lastStatus = conversation.lastStatus.copy(favourited = favourite)
                )

                database.conversationDao().insert(newConversation)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "failed to favourite status")
            }
        }
    }

    fun bookmark(bookmark: Boolean, conversation: ConversationEntity) {
        viewModelScope.launch {
            try {
                timelineCases.bookmark(conversation.lastStatus.id, bookmark).await()

                val newConversation = conversation.copy(
                    lastStatus = conversation.lastStatus.copy(bookmarked = bookmark)
                )

                database.conversationDao().insert(newConversation)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "failed to bookmark status")
            }
        }
    }

    fun voteInPoll(choices: List<Int>, conversation: ConversationEntity) {
        viewModelScope.launch {
            try {
                val poll = timelineCases.voteInPoll(conversation.lastStatus.id, conversation.lastStatus.poll?.id!!, choices).await()
                val newConversation = conversation.copy(
                    lastStatus = conversation.lastStatus.copy(poll = poll)
                )

                database.conversationDao().insert(newConversation)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "failed to vote in poll")
            }
        }
    }

    fun expandHiddenStatus(expanded: Boolean, conversation: ConversationEntity) {
        viewModelScope.launch {
            val newConversation = conversation.copy(
                lastStatus = conversation.lastStatus.copy(expanded = expanded)
            )
            saveConversationToDb(newConversation)
        }
    }

    fun collapseLongStatus(collapsed: Boolean, conversation: ConversationEntity) {
        viewModelScope.launch {
            val newConversation = conversation.copy(
                lastStatus = conversation.lastStatus.copy(collapsed = collapsed)
            )
            saveConversationToDb(newConversation)
        }
    }

    fun showContent(showing: Boolean, conversation: ConversationEntity) {
        viewModelScope.launch {
            val newConversation = conversation.copy(
                lastStatus = conversation.lastStatus.copy(showingHiddenContent = showing)
            )
            saveConversationToDb(newConversation)
        }
    }

    fun remove(conversation: ConversationEntity) {
        viewModelScope.launch {
            try {
                api.deleteConversation(conversationId = conversation.id)

                database.conversationDao().delete(conversation)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "failed to delete conversation")
            }
        }
    }

    fun muteConversation(conversation: ConversationEntity) {
        viewModelScope.launch {
            try {
                val newStatus = timelineCases.muteConversation(
                    conversation.lastStatus.id,
                    !conversation.lastStatus.muted
                ).await()

                val newConversation = conversation.copy(
                    lastStatus = newStatus.toEntity()
                )

                database.conversationDao().insert(newConversation)
            } catch (e: Exception) {
                Timber.tag(TAG).w(e, "failed to mute conversation")
            }
        }
    }

    suspend fun saveConversationToDb(conversation: ConversationEntity) {
        database.conversationDao().insert(conversation)
    }

    companion object {
        private const val TAG = "ConversationsViewModel"
    }
}
