// from https://proandroiddev.com/viewmodel-with-dagger2-architecture-components-2e06f06c9455

package com.imzqqq.app.flow.di

import androidx.lifecycle.ViewModel
import com.imzqqq.app.flow.components.announcements.AnnouncementsViewModel
import com.imzqqq.app.flow.components.compose.ComposeViewModel
import com.imzqqq.app.flow.components.conversation.ConversationsViewModel
import com.imzqqq.app.flow.components.drafts.DraftsViewModel
import com.imzqqq.app.flow.components.report.ReportViewModel
import com.imzqqq.app.flow.components.scheduled.ScheduledTootViewModel
import com.imzqqq.app.flow.components.search.SearchViewModel
import com.imzqqq.app.flow.components.timeline.TimelineViewModel
import com.imzqqq.app.flow.viewmodel.AccountViewModel
import com.imzqqq.app.flow.viewmodel.AccountsInListViewModel
import com.imzqqq.app.flow.viewmodel.EditProfileViewModel
import com.imzqqq.app.flow.viewmodel.ListsViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import com.imzqqq.app.core.di.ViewModelKey

@InstallIn(ActivityComponent::class)
@Module
interface FlowViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun accountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    fun editProfileViewModel(viewModel: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConversationsViewModel::class)
    fun conversationsViewModel(viewModel: ConversationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListsViewModel::class)
    fun listsViewModel(viewModel: ListsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountsInListViewModel::class)
    fun accountsInListViewModel(viewModel: AccountsInListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReportViewModel::class)
    fun reportViewModel(viewModel: ReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun searchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComposeViewModel::class)
    fun composeViewModel(viewModel: ComposeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScheduledTootViewModel::class)
    fun scheduledTootViewModel(viewModel: ScheduledTootViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnnouncementsViewModel::class)
    fun announcementsViewModel(viewModel: AnnouncementsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DraftsViewModel::class)
    fun draftsViewModel(viewModel: DraftsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TimelineViewModel::class)
    fun timelineViewModel(viewModel: TimelineViewModel): ViewModel

    // Add more ViewModels here
}
