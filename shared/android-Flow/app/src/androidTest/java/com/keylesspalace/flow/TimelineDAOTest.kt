package com.keylesspalace.flow

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.keylesspalace.flow.components.timeline.TimelineRepository
import com.keylesspalace.flow.db.AppDatabase
import com.keylesspalace.flow.db.TimelineAccountEntity
import com.keylesspalace.flow.db.TimelineDao
import com.keylesspalace.flow.db.TimelineStatusEntity
import com.keylesspalace.flow.db.TimelineStatusWithAccount
import com.keylesspalace.flow.entity.Status
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimelineDAOTest {
    private lateinit var timelineDao: TimelineDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        timelineDao = db.timelineDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertGetStatus() {
        val setOne = makeStatus(statusId = 3)
        val setTwo = makeStatus(statusId = 20, reblog = true)
        val ignoredOne = makeStatus(statusId = 1)
        val ignoredTwo = makeStatus(accountId = 2)

        for ((status, author, reblogger) in listOf(setOne, setTwo, ignoredOne, ignoredTwo)) {
            timelineDao.insertInTransaction(status, author, reblogger)
        }

        val resultsFromDb = timelineDao.getStatusesForAccount(
            setOne.first.timelineUserId,
            maxId = "21", sinceId = ignoredOne.first.serverId, limit = 10
        )
            .blockingGet()

        assertEquals(2, resultsFromDb.size)
        for ((set, fromDb) in listOf(setTwo, setOne).zip(resultsFromDb)) {
            val (status, author, reblogger) = set
            assertEquals(status, fromDb.status)
            assertEquals(author, fromDb.account)
            assertEquals(reblogger, fromDb.reblogAccount)
        }
    }

    @Test
    fun doNotOverwrite() {
        val (status, author) = makeStatus()
        timelineDao.insertInTransaction(status, author, null)

        val placeholder = createPlaceholder(status.serverId, status.timelineUserId)

        timelineDao.insertStatusIfNotThere(placeholder)

        val fromDb = timelineDao.getStatusesForAccount(status.timelineUserId, null, null, 10)
            .blockingGet()
        val result = fromDb.first()

        assertEquals(1, fromDb.size)
        assertEquals(author, result.account)
        assertEquals(status, result.status)
        assertNull(result.reblogAccount)
    }

    @Test
    fun cleanup() {
        val now = System.currentTimeMillis()
        val oldDate = now - TimelineRepository.CLEANUP_INTERVAL - 20_000
        val oldThisAccount = makeStatus(
            statusId = 5,
            createdAt = oldDate
        )
        val oldAnotherAccount = makeStatus(
            statusId = 10,
            createdAt = oldDate,
            accountId = 2
        )
        val recentThisAccount = makeStatus(
            statusId = 30,
            createdAt = System.currentTimeMillis()
        )
        val recentAnotherAccount = makeStatus(
            statusId = 60,
            createdAt = System.currentTimeMillis(),
            accountId = 2
        )

        for ((status, author, reblogAuthor) in listOf(oldThisAccount, oldAnotherAccount, recentThisAccount, recentAnotherAccount)) {
            timelineDao.insertInTransaction(status, author, reblogAuthor)
        }

        timelineDao.cleanup(now - TimelineRepository.CLEANUP_INTERVAL)

        assertEquals(
            listOf(recentThisAccount),
            timelineDao.getStatusesForAccount(1, null, null, 100).blockingGet()
                .map { it.toTriple() }
        )

        assertEquals(
            listOf(recentAnotherAccount),
            timelineDao.getStatusesForAccount(2, null, null, 100).blockingGet()
                .map { it.toTriple() }
        )
    }

    @Test
    fun overwriteDeletedStatus() {

        val oldStatuses = listOf(
            makeStatus(statusId = 3),
            makeStatus(statusId = 2),
            makeStatus(statusId = 1)
        )

        timelineDao.deleteRange(1, oldStatuses.last().first.serverId, oldStatuses.first().first.serverId)

        for ((status, author, reblogAuthor) in oldStatuses) {
            timelineDao.insertInTransaction(status, author, reblogAuthor)
        }

        // status 2 gets deleted, newly loaded status contain only 1 + 3
        val newStatuses = listOf(
            makeStatus(statusId = 3),
            makeStatus(statusId = 1)
        )

        timelineDao.deleteRange(1, newStatuses.last().first.serverId, newStatuses.first().first.serverId)

        for ((status, author, reblogAuthor) in newStatuses) {
            timelineDao.insertInTransaction(status, author, reblogAuthor)
        }

        // make sure status 2 is no longer in db

        assertEquals(
            newStatuses,
            timelineDao.getStatusesForAccount(1, null, null, 100).blockingGet()
                .map { it.toTriple() }
        )
    }

    private fun makeStatus(
        accountId: Long = 1,
        statusId: Long = 10,
        reblog: Boolean = false,
        createdAt: Long = statusId,
        authorServerId: String = "20"
    ): Triple<TimelineStatusEntity, TimelineAccountEntity, TimelineAccountEntity?> {
        val author = TimelineAccountEntity(
            authorServerId,
            accountId,
            "localUsername",
            "username",
            "displayName",
            "blah",
            "avatar",
            "[\"flow\": \"http://flow.cool/emoji.jpg\"]",
            false
        )

        val reblogAuthor = if (reblog) {
            TimelineAccountEntity(
                "R$authorServerId",
                accountId,
                "RlocalUsername",
                "Rusername",
                "RdisplayName",
                "Rblah",
                "Ravatar",
                "[]",
                false
            )
        } else null

        val even = accountId % 2 == 0L
        val status = TimelineStatusEntity(
            serverId = statusId.toString(),
            url = "url$statusId",
            timelineUserId = accountId,
            authorServerId = authorServerId,
            inReplyToId = "inReplyToId$statusId",
            inReplyToAccountId = "inReplyToAccountId$statusId",
            content = "Content!$statusId",
            createdAt = createdAt,
            emojis = "emojis$statusId",
            reblogsCount = 1 * statusId.toInt(),
            favouritesCount = 2 * statusId.toInt(),
            reblogged = even,
            favourited = !even,
            bookmarked = false,
            sensitive = even,
            spoilerText = "spoier$statusId",
            visibility = Status.Visibility.PRIVATE,
            attachments = "attachments$accountId",
            mentions = "mentions$accountId",
            application = "application$accountId",
            reblogServerId = if (reblog) (statusId * 100).toString() else null,
            reblogAccountId = reblogAuthor?.serverId,
            poll = null,
            muted = false
        )
        return Triple(status, author, reblogAuthor)
    }

    private fun createPlaceholder(serverId: String, timelineUserId: Long): TimelineStatusEntity {
        return TimelineStatusEntity(
            serverId = serverId,
            url = null,
            timelineUserId = timelineUserId,
            authorServerId = null,
            inReplyToId = null,
            inReplyToAccountId = null,
            content = null,
            createdAt = 0L,
            emojis = null,
            reblogsCount = 0,
            favouritesCount = 0,
            reblogged = false,
            favourited = false,
            bookmarked = false,
            sensitive = false,
            spoilerText = null,
            visibility = null,
            attachments = null,
            mentions = null,
            application = null,
            reblogServerId = null,
            reblogAccountId = null,
            poll = null,
            muted = false
        )
    }

    private fun TimelineStatusWithAccount.toTriple() = Triple(status, account, reblogAccount)
}
