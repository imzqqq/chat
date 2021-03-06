package com.imzqqq.app.flow.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.imzqqq.app.flow.appstore.BlockEvent
import com.imzqqq.app.flow.appstore.DomainMuteEvent
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.MuteEvent
import com.imzqqq.app.flow.appstore.ProfileEditedEvent
import com.imzqqq.app.flow.appstore.UnfollowEvent
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.entity.Field
import com.imzqqq.app.flow.entity.IdentityProof
import com.imzqqq.app.flow.entity.Relationship
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.Either
import com.imzqqq.app.flow.util.Error
import com.imzqqq.app.flow.util.Loading
import com.imzqqq.app.flow.util.Resource
import com.imzqqq.app.flow.util.RxAwareViewModel
import com.imzqqq.app.flow.util.Success
import com.imzqqq.app.flow.util.combineOptionalLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AccountViewModel @Inject constructor(
        private val mastodonApi: MastodonApi,
        private val eventHub: EventHub,
        private val accountManager: AccountManager
) : RxAwareViewModel() {

    val accountData = MutableLiveData<Resource<Account>>()
    val relationshipData = MutableLiveData<Resource<Relationship>>()

    val noteSaved = MutableLiveData<Boolean>()

    private val identityProofData = MutableLiveData<List<IdentityProof>>()

    val accountFieldData = combineOptionalLiveData(accountData, identityProofData) { accountRes, identityProofs ->
        identityProofs.orEmpty().map { Either.Left<IdentityProof, Field>(it) }
            .plus(accountRes?.data?.fields.orEmpty().map { Either.Right(it) })
    }

    val isRefreshing = MutableLiveData<Boolean>().apply { value = false }
    private var isDataLoading = false

    lateinit var accountId: String
    var isSelf = false

    private var noteDisposable: Disposable? = null

    init {
        eventHub.events
            .subscribe { event ->
                if (event is ProfileEditedEvent && event.newProfileData.id == accountData.value?.data?.id) {
                    accountData.postValue(Success(event.newProfileData))
                }
            }.autoDispose()
    }

    private fun obtainAccount(reload: Boolean = false) {
        if (accountData.value == null || reload) {
            isDataLoading = true
            accountData.postValue(Loading())

            mastodonApi.account(accountId)
                .subscribe(
                    { account ->
                        accountData.postValue(Success(account))
                        isDataLoading = false
                        isRefreshing.postValue(false)
                    },
                    { t ->
                        Timber.tag(TAG).w(t, "failed obtaining account")
                        accountData.postValue(Error())
                        isDataLoading = false
                        isRefreshing.postValue(false)
                    }
                )
                .autoDispose()
        }
    }

    private fun obtainRelationship(reload: Boolean = false) {
        if (relationshipData.value == null || reload) {

            relationshipData.postValue(Loading())

            mastodonApi.relationships(listOf(accountId))
                .subscribe(
                    { relationships ->
                        relationshipData.postValue(Success(relationships[0]))
                    },
                    { t ->
                        Timber.tag(TAG).w(t, "failed obtaining relationships")
                        relationshipData.postValue(Error())
                    }
                )
                .autoDispose()
        }
    }

    private fun obtainIdentityProof(reload: Boolean = false) {
        if (identityProofData.value == null || reload) {

            mastodonApi.identityProofs(accountId)
                .subscribe(
                    { proofs ->
                        identityProofData.postValue(proofs)
                    },
                    { t ->
                        Timber.tag(TAG).w(t, "failed obtaining identity proofs")
                    }
                )
                .autoDispose()
        }
    }

    fun changeFollowState() {
        val relationship = relationshipData.value?.data
        if (relationship?.following == true || relationship?.requested == true) {
            changeRelationship(RelationShipAction.UNFOLLOW)
        } else {
            changeRelationship(RelationShipAction.FOLLOW)
        }
    }

    fun changeBlockState() {
        if (relationshipData.value?.data?.blocking == true) {
            changeRelationship(RelationShipAction.UNBLOCK)
        } else {
            changeRelationship(RelationShipAction.BLOCK)
        }
    }

    fun muteAccount(notifications: Boolean, duration: Int?) {
        changeRelationship(RelationShipAction.MUTE, notifications, duration)
    }

    fun unmuteAccount() {
        changeRelationship(RelationShipAction.UNMUTE)
    }

    fun changeSubscribingState() {
        val relationship = relationshipData.value?.data
        if (relationship?.notifying == true || /* Mastodon 3.3.0rc1 */
            relationship?.subscribing == true /* Pleroma */
        ) {
            changeRelationship(RelationShipAction.UNSUBSCRIBE)
        } else {
            changeRelationship(RelationShipAction.SUBSCRIBE)
        }
    }

    fun blockDomain(instance: String) {
        mastodonApi.blockDomain(instance).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    eventHub.dispatch(DomainMuteEvent(instance))
                    val relation = relationshipData.value?.data
                    if (relation != null) {
                        relationshipData.postValue(Success(relation.copy(blockingDomain = true)))
                    }
                } else {
                    Timber.e("Error muting %s".format(instance))
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Timber.e(t, "Error muting %s".format(instance))
            }
        })
    }

    fun unblockDomain(instance: String) {
        mastodonApi.unblockDomain(instance).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val relation = relationshipData.value?.data
                    if (relation != null) {
                        relationshipData.postValue(Success(relation.copy(blockingDomain = false)))
                    }
                } else {
                    Timber.e("Error unmuting %s".format(instance))
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                Timber.e(t, "Error unmuting %s".format(instance))
            }
        })
    }

    fun changeShowReblogsState() {
        if (relationshipData.value?.data?.showingReblogs == true) {
            changeRelationship(RelationShipAction.FOLLOW, false)
        } else {
            changeRelationship(RelationShipAction.FOLLOW, true)
        }
    }

    /**
     * @param parameter showReblogs if RelationShipAction.FOLLOW, notifications if MUTE
     */
    private fun changeRelationship(relationshipAction: RelationShipAction, parameter: Boolean? = null, duration: Int? = null) {
        val relation = relationshipData.value?.data
        val account = accountData.value?.data
        val isMastodon = relationshipData.value?.data?.notifying != null

        if (relation != null && account != null) {
            // optimistically post new state for faster response

            val newRelation = when (relationshipAction) {
                RelationShipAction.FOLLOW      -> {
                    if (account.locked) {
                        relation.copy(requested = true)
                    } else {
                        relation.copy(following = true)
                    }
                }
                RelationShipAction.UNFOLLOW    -> relation.copy(following = false)
                RelationShipAction.BLOCK       -> relation.copy(blocking = true)
                RelationShipAction.UNBLOCK     -> relation.copy(blocking = false)
                RelationShipAction.MUTE        -> relation.copy(muting = true)
                RelationShipAction.UNMUTE      -> relation.copy(muting = false)
                RelationShipAction.SUBSCRIBE   -> {
                    if (isMastodon)
                        relation.copy(notifying = true)
                    else relation.copy(subscribing = true)
                }
                RelationShipAction.UNSUBSCRIBE -> {
                    if (isMastodon)
                        relation.copy(notifying = false)
                    else relation.copy(subscribing = false)
                }
            }
            relationshipData.postValue(Loading(newRelation))
        }

        when (relationshipAction) {
            RelationShipAction.FOLLOW      -> mastodonApi.followAccount(accountId, showReblogs = parameter ?: true)
            RelationShipAction.UNFOLLOW    -> mastodonApi.unfollowAccount(accountId)
            RelationShipAction.BLOCK       -> mastodonApi.blockAccount(accountId)
            RelationShipAction.UNBLOCK     -> mastodonApi.unblockAccount(accountId)
            RelationShipAction.MUTE        -> mastodonApi.muteAccount(accountId, parameter ?: true, duration)
            RelationShipAction.UNMUTE      -> mastodonApi.unmuteAccount(accountId)
            RelationShipAction.SUBSCRIBE   -> {
                if (isMastodon)
                    mastodonApi.followAccount(accountId, notify = true)
                else mastodonApi.subscribeAccount(accountId)
            }
            RelationShipAction.UNSUBSCRIBE -> {
                if (isMastodon)
                    mastodonApi.followAccount(accountId, notify = false)
                else mastodonApi.unsubscribeAccount(accountId)
            }
        }.subscribe(
            { relationship ->
                relationshipData.postValue(Success(relationship))

                when (relationshipAction) {
                    RelationShipAction.UNFOLLOW -> eventHub.dispatch(UnfollowEvent(accountId))
                    RelationShipAction.BLOCK    -> eventHub.dispatch(BlockEvent(accountId))
                    RelationShipAction.MUTE     -> eventHub.dispatch(MuteEvent(accountId))
                    else                        -> {
                    }
                }
            },
            {
                relationshipData.postValue(Error(relation))
            }
        )
            .autoDispose()
    }

    fun noteChanged(newNote: String) {
        noteSaved.postValue(false)
        noteDisposable?.dispose()
        noteDisposable = Single.timer(1500, TimeUnit.MILLISECONDS)
            .flatMap {
                mastodonApi.updateAccountNote(accountId, newNote)
            }
            .doOnSuccess {
                noteSaved.postValue(true)
            }
            .delay(4, TimeUnit.SECONDS)
            .subscribe(
                {
                    noteSaved.postValue(false)
                },
                {
                    Timber.e(it, "Error updating note")
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        noteDisposable?.dispose()
    }

    fun refresh() {
        reload(true)
    }

    private fun reload(isReload: Boolean = false) {
        if (isDataLoading)
            return
        accountId.let {
            obtainAccount(isReload)
            obtainIdentityProof()
            if (!isSelf)
                obtainRelationship(isReload)
        }
    }

    fun setAccountInfo(accountId: String) {
        this.accountId = accountId
        this.isSelf = accountManager.activeAccount?.accountId == accountId
        reload(false)
    }

    enum class RelationShipAction {
        FOLLOW, UNFOLLOW, BLOCK, UNBLOCK, MUTE, UNMUTE, SUBSCRIBE, UNSUBSCRIBE
    }

    companion object {
        const val TAG = "AccountViewModel"
    }
}
