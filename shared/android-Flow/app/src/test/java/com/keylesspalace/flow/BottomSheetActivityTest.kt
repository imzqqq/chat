/* Copyright 2018 Levi Bard
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

package com.keylesspalace.flow

import android.text.SpannedString
import android.widget.LinearLayout
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.keylesspalace.flow.entity.Account
import com.keylesspalace.flow.entity.SearchResult
import com.keylesspalace.flow.entity.Status
import com.keylesspalace.flow.network.FlowApi
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.concurrent.TimeUnit

class BottomSheetActivityTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: FakeBottomSheetActivity
    private lateinit var apiMock: FlowApi
    private val accountQuery = "http://flow.imzqqq.top/@User"
    private val statusQuery = "http://flow.imzqqq.top/@User/345678"
    private val nonFlowQuery = "http://medium.com/@correspondent/345678"
    private val emptyCallback = Single.just(SearchResult(emptyList(), emptyList(), emptyList()))
    private val testScheduler = TestScheduler()

    private val account = Account(
        "1",
        "admin",
        "admin",
        "Ad Min",
        SpannedString(""),
        "http://flow.imzqqq.top",
        "",
        "",
        false,
        0,
        0,
        0,
        null,
        false,
        emptyList(),
        emptyList()
    )
    private val accountSingle = Single.just(SearchResult(listOf(account), emptyList(), emptyList()))

    private val status = Status(
        "1",
        statusQuery,
        account,
        null,
        null,
        null,
        SpannedString("omgwat"),
        Date(),
        Collections.emptyList(),
        0,
        0,
        false,
        false,
        false,
        false,
        "",
        Status.Visibility.PUBLIC,
        ArrayList(),
        listOf(),
        null,
        pinned = false,
        muted = false,
        poll = null,
        card = null
    )
    private val statusSingle = Single.just(SearchResult(emptyList(), listOf(status), emptyList()))

    @Before
    fun setup() {

        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxAndroidPlugins.setMainThreadSchedulerHandler { testScheduler }

        apiMock = mock(FlowApi::class.java)
        `when`(apiMock.searchObservable(eq(accountQuery), eq(null), ArgumentMatchers.anyBoolean(), eq(null), eq(null), eq(null))).thenReturn(accountSingle)
        `when`(apiMock.searchObservable(eq(statusQuery), eq(null), ArgumentMatchers.anyBoolean(), eq(null), eq(null), eq(null))).thenReturn(statusSingle)
        `when`(apiMock.searchObservable(eq(nonFlowQuery), eq(null), ArgumentMatchers.anyBoolean(), eq(null), eq(null), eq(null))).thenReturn(emptyCallback)

        activity = FakeBottomSheetActivity(apiMock)
    }

    @RunWith(Parameterized::class)
    class UrlMatchingTests(private val url: String, private val expectedResult: Boolean) {
        companion object {
            @Parameterized.Parameters(name = "match_{0}")
            @JvmStatic
            fun data(): Iterable<Any> {
                return listOf(
                    arrayOf("https://flow.imzqqq.top/@User", true),
                    arrayOf("http://flow.imzqqq.top/@abc123", true),
                    arrayOf("https://flow.imzqqq.top/@user/345667890345678", true),
                    arrayOf("https://flow.imzqqq.top/@user/3", true),
                    arrayOf("https://pleroma.foo.bar/users/meh3223", true),
                    arrayOf("https://pleroma.foo.bar/users/meh3223_bruh", true),
                    arrayOf("https://pleroma.foo.bar/users/2345", true),
                    arrayOf("https://pleroma.foo.bar/notice/9", true),
                    arrayOf("https://pleroma.foo.bar/notice/9345678", true),
                    arrayOf("https://pleroma.foo.bar/notice/wat", true),
                    arrayOf("https://pleroma.foo.bar/notice/9qTHT2ANWUdXzENqC0", true),
                    arrayOf("https://pleroma.foo.bar/objects/abcdef-123-abcd-9876543", true),
                    arrayOf("https://misskey.foo.bar/notes/mew", true),
                    arrayOf("https://misskey.foo.bar/notes/1421564653", true),
                    arrayOf("https://misskey.foo.bar/notes/qwer615985ddf", true),
                    arrayOf("https://friendica.foo.bar/profile/user", true),
                    arrayOf("https://friendica.foo.bar/profile/uSeR", true),
                    arrayOf("https://friendica.foo.bar/profile/user_user", true),
                    arrayOf("https://friendica.foo.bar/profile/123", true),
                    arrayOf("https://friendica.foo.bar/display/abcdef-123-abcd-9876543", true),
                    arrayOf("https://google.com/", false),
                    arrayOf("https://flow.imzqqq.top/@User?foo=bar", false),
                    arrayOf("https://flow.imzqqq.top/@User#foo", false),
                    arrayOf("http://flow.imzqqq.top/@", false),
                    arrayOf("http://flow.imzqqq.top/@/345678", false),
                    arrayOf("https://flow.imzqqq.top/@user/345667890345678/", false),
                    arrayOf("https://flow.imzqqq.top/@user/3abce", false),
                    arrayOf("https://pleroma.foo.bar/users/", false),
                    arrayOf("https://pleroma.foo.bar/users/meow/", false),
                    arrayOf("https://pleroma.foo.bar/users/@meow", false),
                    arrayOf("https://pleroma.foo.bar/user/2345", false),
                    arrayOf("https://pleroma.foo.bar/notices/123456", false),
                    arrayOf("https://pleroma.foo.bar/notice/@neverhappen/", false),
                    arrayOf("https://pleroma.foo.bar/object/abcdef-123-abcd-9876543", false),
                    arrayOf("https://pleroma.foo.bar/objects/xabcdef-123-abcd-9876543", false),
                    arrayOf("https://pleroma.foo.bar/objects/xabcdef-123-abcd-9876543/", false),
                    arrayOf("https://pleroma.foo.bar/objects/xabcdef-123-abcd_9876543", false),
                    arrayOf("https://friendica.foo.bar/display/xabcdef-123-abcd-9876543", false),
                    arrayOf("https://friendica.foo.bar/display/xabcdef-123-abcd-9876543/", false),
                    arrayOf("https://friendica.foo.bar/display/xabcdef-123-abcd_9876543", false),
                    arrayOf("https://friendica.foo.bar/profile/@mew", false),
                    arrayOf("https://friendica.foo.bar/profile/@mew/", false),
                    arrayOf("https://misskey.foo.bar/notes/@nyan", false),
                    arrayOf("https://misskey.foo.bar/notes/NYAN123", false),
                    arrayOf("https://misskey.foo.bar/notes/meow123/", false)
                )
            }
        }

        @Test
        fun test() {
            Assert.assertEquals(expectedResult, looksLikeFlowUrl(url))
        }
    }

    @Test
    fun beginEndSearch_setIsSearching_isSearchingAfterBegin() {
        activity.onBeginSearch("https://flow.imzqqq.top/@User")
        Assert.assertTrue(activity.isSearching())
    }

    @Test
    fun beginEndSearch_setIsSearching_isNotSearchingAfterEnd() {
        val validUrl = "https://flow.imzqqq.top/@User"
        activity.onBeginSearch(validUrl)
        activity.onEndSearch(validUrl)
        Assert.assertFalse(activity.isSearching())
    }

    @Test
    fun beginEndSearch_setIsSearching_doesNotCancelSearchWhenResponseFromPreviousSearchIsReceived() {
        val validUrl = "https://flow.imzqqq.top/@User"
        val invalidUrl = ""

        activity.onBeginSearch(validUrl)
        activity.onEndSearch(invalidUrl)
        Assert.assertTrue(activity.isSearching())
    }

    @Test
    fun cancelActiveSearch() {
        val url = "https://flow.imzqqq.top/@User"

        activity.onBeginSearch(url)
        activity.cancelActiveSearch()
        Assert.assertFalse(activity.isSearching())
    }

    @Test
    fun getCancelSearchRequested_detectsURL() {
        val firstUrl = "https://flow.imzqqq.top/@User"
        val secondUrl = "https://flow.imzqqq.top/@meh"

        activity.onBeginSearch(firstUrl)
        activity.cancelActiveSearch()

        activity.onBeginSearch(secondUrl)
        Assert.assertTrue(activity.getCancelSearchRequested(firstUrl))
        Assert.assertFalse(activity.getCancelSearchRequested(secondUrl))
    }

    @Test
    fun search_inIdealConditions_returnsRequestedResults_forAccount() {
        activity.viewUrl(accountQuery)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        Assert.assertEquals(account.id, activity.accountId)
    }

    @Test
    fun search_inIdealConditions_returnsRequestedResults_forStatus() {
        activity.viewUrl(statusQuery)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        Assert.assertEquals(status.id, activity.statusId)
    }

    @Test
    fun search_inIdealConditions_returnsRequestedResults_forNonFlowURL() {
        activity.viewUrl(nonFlowQuery)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        Assert.assertEquals(nonFlowQuery, activity.link)
    }

    @Test
    fun search_withNoResults_appliesRequestedFallbackBehavior() {
        for (fallbackBehavior in listOf(PostLookupFallbackBehavior.OPEN_IN_BROWSER, PostLookupFallbackBehavior.DISPLAY_ERROR)) {
            activity.viewUrl(nonFlowQuery, fallbackBehavior)
            testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
            Assert.assertEquals(nonFlowQuery, activity.link)
            Assert.assertEquals(fallbackBehavior, activity.fallbackBehavior)
        }
    }

    @Test
    fun search_withCancellation_doesNotLoadUrl_forAccount() {
        activity.viewUrl(accountQuery)
        Assert.assertTrue(activity.isSearching())
        activity.cancelActiveSearch()
        Assert.assertFalse(activity.isSearching())
        Assert.assertEquals(null, activity.accountId)
    }

    @Test
    fun search_withCancellation_doesNotLoadUrl_forStatus() {
        activity.viewUrl(accountQuery)
        activity.cancelActiveSearch()
        Assert.assertEquals(null, activity.accountId)
    }

    @Test
    fun search_withCancellation_doesNotLoadUrl_forNonFlowURL() {
        activity.viewUrl(nonFlowQuery)
        activity.cancelActiveSearch()
        Assert.assertEquals(null, activity.searchUrl)
    }

    @Test
    fun search_withPreviousCancellation_completes() {
        // begin/cancel account search
        activity.viewUrl(accountQuery)
        activity.cancelActiveSearch()

        // begin status search
        activity.viewUrl(statusQuery)

        // ensure that search is still ongoing
        Assert.assertTrue(activity.isSearching())

        // return searchResults
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)

        // ensure that the result of the status search was recorded
        // and the account search wasn't
        Assert.assertEquals(status.id, activity.statusId)
        Assert.assertEquals(null, activity.accountId)
    }

    class FakeBottomSheetActivity(api: FlowApi) : BottomSheetActivity() {

        var statusId: String? = null
        var accountId: String? = null
        var link: String? = null
        var fallbackBehavior: PostLookupFallbackBehavior? = null

        init {
            flowApi = api
            @Suppress("UNCHECKED_CAST")
            bottomSheet = mock(BottomSheetBehavior::class.java) as BottomSheetBehavior<LinearLayout>
        }

        override fun openLink(url: String) {
            this.link = url
        }

        override fun viewAccount(id: String) {
            this.accountId = id
        }

        override fun viewThread(statusId: String, url: String?) {
            this.statusId = statusId
        }

        override fun performUrlFallbackAction(url: String, fallbackBehavior: PostLookupFallbackBehavior) {
            this.link = url
            this.fallbackBehavior = fallbackBehavior
        }
    }
}
