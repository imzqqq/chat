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

package com.keylesspalace.flow.components.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.keylesspalace.flow.db.AccountManager
import com.keylesspalace.flow.db.AppDatabase
import com.keylesspalace.flow.db.DraftEntity
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.network.FlowApi
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.launch
import javax.inject.Inject

class DraftsViewModel @Inject constructor(
    val database: AppDatabase,
    val accountManager: AccountManager,
    val api: FlowApi,
    val draftHelper: DraftHelper
) : ViewModel() {

    val drafts = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { database.draftDao().draftsPagingSource(accountManager.activeAccount?.id!!) }
    ).flow
        .cachedIn(viewModelScope)

    private val deletedDrafts: MutableList<DraftEntity> = mutableListOf()

    fun deleteDraft(draft: DraftEntity) {
        // this does not immediately delete media files to avoid unnecessary file operations
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
