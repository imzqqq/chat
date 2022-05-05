package com.imzqqq.app.flow.db

import android.text.Spanned
import androidx.core.text.parseAsHtml
import androidx.core.text.toHtml
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.imzqqq.app.flow.TabData
import com.imzqqq.app.flow.components.conversation.ConversationAccountEntity
import com.imzqqq.app.flow.createTabDataFromId
import com.imzqqq.app.flow.entity.Attachment
import com.imzqqq.app.flow.entity.Emoji
import com.imzqqq.app.flow.entity.NewPoll
import com.imzqqq.app.flow.entity.Poll
import com.imzqqq.app.flow.entity.Status
import com.imzqqq.app.flow.util.trimTrailingWhitespace
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.ArrayList
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@ProvidedTypeConverter
@Singleton
class Converters @Inject constructor (
    private val gson: Gson
) {

    @TypeConverter
    fun jsonToEmojiList(emojiListJson: String?): List<Emoji>? {
        return gson.fromJson(emojiListJson, object : TypeToken<List<Emoji>>() {}.type)
    }

    @TypeConverter
    fun emojiListToJson(emojiList: List<Emoji>?): String {
        return gson.toJson(emojiList)
    }

    @TypeConverter
    fun visibilityToInt(visibility: Status.Visibility?): Int {
        return visibility?.num ?: Status.Visibility.UNKNOWN.num
    }

    @TypeConverter
    fun intToVisibility(visibility: Int): Status.Visibility {
        return Status.Visibility.byNum(visibility)
    }

    @TypeConverter
    fun stringToTabData(str: String?): List<TabData>? {
        return str?.split(";")
            ?.map {
                val data = it.split(":")
                createTabDataFromId(data[0], data.drop(1).map { s -> URLDecoder.decode(s, "UTF-8") })
            }
    }

    @TypeConverter
    fun tabDataToString(tabData: List<TabData>?): String? {
        // List name may include ":"
        return tabData?.joinToString(";") { it.id + ":" + it.arguments.joinToString(":") { s -> URLEncoder.encode(s, "UTF-8") } }
    }

    @TypeConverter
    fun accountToJson(account: ConversationAccountEntity?): String {
        return gson.toJson(account)
    }

    @TypeConverter
    fun jsonToAccount(accountJson: String?): ConversationAccountEntity? {
        return gson.fromJson(accountJson, ConversationAccountEntity::class.java)
    }

    @TypeConverter
    fun accountListToJson(accountList: List<ConversationAccountEntity>?): String {
        return gson.toJson(accountList)
    }

    @TypeConverter
    fun jsonToAccountList(accountListJson: String?): List<ConversationAccountEntity>? {
        return gson.fromJson(accountListJson, object : TypeToken<List<ConversationAccountEntity>>() {}.type)
    }

    @TypeConverter
    fun attachmentListToJson(attachmentList: List<Attachment>?): String {
        return gson.toJson(attachmentList)
    }

    @TypeConverter
    fun jsonToAttachmentList(attachmentListJson: String?): ArrayList<Attachment>? {
        return gson.fromJson(attachmentListJson, object : TypeToken<ArrayList<Attachment>>() {}.type)
    }

    @TypeConverter
    fun mentionListToJson(mentionArray: List<Status.Mention>?): String? {
        return gson.toJson(mentionArray)
    }

    @TypeConverter
    fun jsonToMentionArray(mentionListJson: String?): List<Status.Mention>? {
        return gson.fromJson(mentionListJson, object : TypeToken<List<Status.Mention>>() {}.type)
    }

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(date: Long): Date {
        return Date(date)
    }

    @TypeConverter
    fun spannedToString(spanned: Spanned?): String? {
        if (spanned == null) {
            return null
        }
        return spanned.toHtml()
    }

    @TypeConverter
    fun stringToSpanned(spannedString: String?): Spanned? {
        if (spannedString == null) {
            return null
        }
        return spannedString.parseAsHtml().trimTrailingWhitespace()
    }

    @TypeConverter
    fun pollToJson(poll: Poll?): String? {
        return gson.toJson(poll)
    }

    @TypeConverter
    fun jsonToPoll(pollJson: String?): Poll? {
        return gson.fromJson(pollJson, Poll::class.java)
    }

    @TypeConverter
    fun newPollToJson(newPoll: NewPoll?): String? {
        return gson.toJson(newPoll)
    }

    @TypeConverter
    fun jsonToNewPoll(newPollJson: String?): NewPoll? {
        return gson.fromJson(newPollJson, NewPoll::class.java)
    }

    @TypeConverter
    fun draftAttachmentListToJson(draftAttachments: List<DraftAttachment>?): String? {
        return gson.toJson(draftAttachments)
    }

    @TypeConverter
    fun jsonToDraftAttachmentList(draftAttachmentListJson: String?): List<DraftAttachment>? {
        return gson.fromJson(draftAttachmentListJson, object : TypeToken<List<DraftAttachment>>() {}.type)
    }
}
