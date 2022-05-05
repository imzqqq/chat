/// MARK
@file:Suppress("DEPRECATION")
///

package com.imzqqq.app.flow.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.core.content.ContextCompat
import com.imzqqq.app.R
import com.imzqqq.app.flow.components.compose.ComposeActivity
import com.imzqqq.app.flow.components.compose.ComposeActivity.ComposeOptions
import com.imzqqq.app.flow.components.notifications.NotificationHelper
import com.imzqqq.app.flow.db.AccountManager
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.service.SendTootService
import com.imzqqq.app.flow.service.TootToSend
import com.imzqqq.app.flow.util.randomAlphanumericString
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "SendStatusBR"

class SendStatusBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var accountManager: AccountManager

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(NotificationHelper.KEY_NOTIFICATION_ID, -1)
        val senderId = intent.getLongExtra(NotificationHelper.KEY_SENDER_ACCOUNT_ID, -1)
        val senderIdentifier = intent.getStringExtra(NotificationHelper.KEY_SENDER_ACCOUNT_IDENTIFIER)
        val senderFullName = intent.getStringExtra(NotificationHelper.KEY_SENDER_ACCOUNT_FULL_NAME)
        val citedStatusId = intent.getStringExtra(NotificationHelper.KEY_CITED_STATUS_ID)
        val visibility = intent.getSerializableExtra(NotificationHelper.KEY_VISIBILITY) as Status.Visibility
        val spoiler = intent.getStringExtra(NotificationHelper.KEY_SPOILER) ?: ""
        val mentions = intent.getStringArrayExtra(NotificationHelper.KEY_MENTIONS) ?: emptyArray()
        val citedText = intent.getStringExtra(NotificationHelper.KEY_CITED_TEXT)
        val localAuthorId = intent.getStringExtra(NotificationHelper.KEY_CITED_AUTHOR_LOCAL)

        val account = accountManager.getAccountById(senderId)

        val notificationManager = NotificationManagerCompat.from(context)

        if (intent.action == NotificationHelper.REPLY_ACTION) {

            val message = getReplyMessage(intent)

            if (account == null) {
                Timber.tag(TAG).w("Account \"$senderId\" not found in database. Aborting quick reply!")

                val builder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_MENTION + senderIdentifier)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setColor(ContextCompat.getColor(context, R.color.tusky_blue))
                    .setGroup(senderFullName)
                    .setDefaults(0) // So it doesn't ring twice, notify only in Target callback

                builder.setContentTitle(context.getString(R.string.error_generic))
                builder.setContentText(context.getString(R.string.error_sender_account_gone))

                builder.setSubText(senderFullName)
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                builder.setCategory(NotificationCompat.CATEGORY_SOCIAL)
                builder.setOnlyAlertOnce(true)

                notificationManager.notify(notificationId, builder.build())
            } else {
                val text = mentions.joinToString(" ", postfix = " ") { "@$it" } + message.toString()

                val sendIntent = SendTootService.sendTootIntent(
                    context,
                    TootToSend(
                        text = text,
                        warningText = spoiler,
                        visibility = visibility.serverString(),
                        sensitive = false,
                        mediaIds = emptyList(),
                        mediaUris = emptyList(),
                        mediaDescriptions = emptyList(),
                        scheduledAt = null,
                        inReplyToId = citedStatusId,
                        poll = null,
                        replyingStatusContent = null,
                        replyingStatusAuthorUsername = null,
                        accountId = account.id,
                        draftId = -1,
                        idempotencyKey = randomAlphanumericString(16),
                        retries = 0
                    )
                )

                context.startService(sendIntent)

                val builder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_MENTION + senderIdentifier)
                    .setSmallIcon(R.drawable.ic_notify)
                    .setColor(ContextCompat.getColor(context, (R.color.tusky_blue)))
                    .setGroup(senderFullName)
                    .setDefaults(0) // So it doesn't ring twice, notify only in Target callback

                builder.setContentTitle(context.getString(R.string.status_sent))
                builder.setContentText(context.getString(R.string.status_sent_long))

                builder.setSubText(senderFullName)
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                builder.setCategory(NotificationCompat.CATEGORY_SOCIAL)
                builder.setOnlyAlertOnce(true)

                notificationManager.notify(notificationId, builder.build())
            }
        } else if (intent.action == NotificationHelper.COMPOSE_ACTION) {

            context.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))

            notificationManager.cancel(notificationId)

            accountManager.setActiveAccount(senderId)

            val composeIntent = ComposeActivity.startIntent(
                context,
                ComposeOptions(
                    inReplyToId = citedStatusId,
                    replyVisibility = visibility,
                    contentWarning = spoiler,
                    mentionedUsernames = mentions.toSet(),
                    replyingStatusAuthor = localAuthorId,
                    replyingStatusContent = citedText
                )
            )

            composeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(composeIntent)
        }
    }

    private fun getReplyMessage(intent: Intent): CharSequence {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        return remoteInput.getCharSequence(NotificationHelper.KEY_REPLY, "")
    }
}
