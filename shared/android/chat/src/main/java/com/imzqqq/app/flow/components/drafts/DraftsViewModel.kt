package com.imzqqq.app.flow.components.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.db.AppDatabase
import com.imzqqq.app.flow.db.DraftEntity
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.network.MastodonApi
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    val database: AppDatabase,
    val accountManager: AccountManager,
    val api: MastodonApi,
    val draftHelper: DraftHelper
) : ViewModel() {

    val drafts = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { database.draftDao().draftsPagingSource(accountManager.activeAccount?.id!!) }
    ).flow
        .cachedIn(viewModelScope)

    private val deletedDrafts: MutableList<DraftEntity> = mutableListOf()

    fun deleteDraft(draft: DraftEntity) {
        // This does not immediately delete media files to avoid unnecessary file operations
        // in case the user decides to restore the draft
        viewModelScope.launch {
            database.draftDao().delete(draft.id)
            deletedDrafts.add(draft)
        }
    }

    fun restoreDraft(draft: DraftEntity) {
        viewModelScope.launch {
            database.draftDao().insertOrReplace(draft)
            deletedDrafts.remove(draft)
        }
    }

    fun getToot(tootId: String): Single<Status> {
        return api.status(tootId)
    }

    override fun onCleared() {
        viewModelScope.launch {
            deletedDrafts.forEach {
                draftHelper.deleteAttachments(it)
            }
        }
    }
}
