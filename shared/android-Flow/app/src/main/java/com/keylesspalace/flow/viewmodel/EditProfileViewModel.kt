/* Copyright 2018 Conny Duck
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

package com.keylesspalace.flow.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keylesspalace.flow.EditProfileActivity.Companion.AVATAR_SIZE
import com.keylesspalace.flow.EditProfileActivity.Companion.HEADER_HEIGHT
import com.keylesspalace.flow.EditProfileActivity.Companion.HEADER_WIDTH
import com.keylesspalace.flow.appstore.EventHub
import com.keylesspalace.flow.appstore.ProfileEditedEvent
import com.keylesspalace.flow.entity.Account
import com.keylesspalace.flow.entity.Instance
import com.keylesspalace.flow.entity.StringField
import com.keylesspalace.flow.network.FlowApi
import com.keylesspalace.flow.util.Error
import com.keylesspalace.flow.util.IOUtils
import com.keylesspalace.flow.util.Loading
import com.keylesspalace.flow.util.Resource
import com.keylesspalace.flow.util.Success
import com.keylesspalace.flow.util.getSampledBitmap
import com.keylesspalace.flow.util.randomAlphanumericString
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

private const val HEADER_FILE_NAME = "header.png"
private const val AVATAR_FILE_NAME = "avatar.png"

private const val TAG = "EditProfileViewModel"

class EditProfileViewModel @Inject constructor(
    private val flowApi: FlowApi,
    private val eventHub: EventHub
) : ViewModel() {

    val profileData = MutableLiveData<Resource<Account>>()
    val avatarData = MutableLiveData<Resource<Bitmap>>()
    val headerData = MutableLiveData<Resource<Bitmap>>()
    val saveData = MutableLiveData<Resource<Nothing>>()
    val instanceData = MutableLiveData<Resource<Instance>>()

    private var oldProfileData: Account? = null

    private val disposeables = CompositeDisposable()

    fun obtainProfile() {
        if (profileData.value == null || profileData.value is Error) {

            profileData.postValue(Loading())

            flowApi.accountVerifyCredentials()
                .subscribe(
                    { profile ->
                        oldProfileData = profile
                        profileData.postValue(Success(profile))
                    },
                    {
                        profileData.postValue(Error())
                    }
                )
                .addTo(disposeables)
        }
    }

    fun newAvatar(uri: Uri, context: Context) {
        val cacheFile = getCacheFileForName(context, AVATAR_FILE_NAME)

        resizeImage(uri, context, AVATAR_SIZE, AVATAR_SIZE, cacheFile, avatarData)
    }

    fun newHeader(uri: Uri, context: Context) {
        val cacheFile = getCacheFileForName(context, HEADER_FILE_NAME)

        resizeImage(uri, context, HEADER_WIDTH, HEADER_HEIGHT, cacheFile, headerData)
    }

    private fun resizeImage(
        uri: Uri,
        context: Context,
        resizeWidth: Int,
        resizeHeight: Int,
        cacheFile: File,
        imageLiveData: MutableLiveData<Resource<Bitmap>>
    ) {

        Single.fromCallable {
            val contentResolver = context.contentResolver
            val sourceBitmap = getSampledBitmap(contentResolver, uri, resizeWidth, resizeHeight)

            if (sourceBitmap == null) {
                throw Exception()
            }

            // dont upscale image if its smaller than the desired size
            val bitmap =
                if (sourceBitmap.width <= resizeWidth && sourceBitmap.height <= resizeHeight) {
                    sourceBitmap
                } else {
                    Bitmap.createScaledBitmap(sourceBitmap, resizeWidth, resizeHeight, true)
                }

            if (!saveBitmapToFile(bitmap, cacheFile)) {
                throw Exception()
            }

            bitmap
        }.subscribeOn(Schedulers.io())
            .subscribe(
                {
                    imageLiveData.postValue(Success(it))
                },
                {
                    imageLiveData.postValue(Error())
                }
            )
            .addTo(disposeables)
    }

    fun save(newDisplayName: String, newNote: String, newLocked: Boolean, newFields: List<StringField>, context: Context) {

        if (saveData.value is Loading || profileData.value !is Success) {
            return
        }

        val displayName = if (oldProfileData?.displayName == newDisplayName) {
            null
        } else {
            newDisplayName.toRequestBody(MultipartBody.FORM)
        }

        val note = if (oldProfileData?.source?.note == newNote) {
            null
        } else {
            newNote.toRequestBody(MultipartBody.FORM)
        }

        val locked = if (oldProfileData?.locked == newLocked) {
            null
        } else {
            newLocked.toString().toRequestBody(MultipartBody.FORM)
        }

        val avatar = if (avatarData.value is Success && avatarData.value?.data != null) {
            val avatarBody = getCacheFileForName(context, AVATAR_FILE_NAME).asRequestBody("image/png".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("avatar", randomAlphanumericString(12), avatarBody)
        } else {
            null
        }

        val header = if (headerData.value is Success && headerData.value?.data != null) {
            val headerBody = getCacheFileForName(context, HEADER_FILE_NAME).asRequestBody("image/png".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("header", randomAlphanumericString(12), headerBody)
        } else {
            null
        }

        // when one field changed, all have to be sent or they unchanged ones would get overridden
        val fieldsUnchanged = oldProfileData?.source?.fields == newFields
        val field1 = calculateFieldToUpdate(newFields.getOrNull(0), fieldsUnchanged)
        val field2 = calculateFieldToUpdate(newFields.getOrNull(1), fieldsUnchanged)
        val field3 = calculateFieldToUpdate(newFields.getOrNull(2), fieldsUnchanged)
        val field4 = calculateFieldToUpdate(newFields.getOrNull(3), fieldsUnchanged)

        if (displayName == null && note == null && locked == null && avatar == null && header == null &&
            field1 == null && field2 == null && field3 == null && field4 == null
        ) {
            /** if nothing has changed, there is no need to make a network request */
            saveData.postValue(Success())
            return
        }

        flowApi.accountUpdateCredentials(
            displayName, note, locked, avatar, header,
            field1?.first, field1?.second, field2?.first, field2?.second, field3?.first, field3?.second, field4?.first, field4?.second
        ).enqueue(object : Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                val newProfileData = response.body()
                if (!response.isSuccessful || newProfileData == null) {
                    val errorResponse = response.errorBody()?.string()
                    val errorMsg = if (!errorResponse.isNullOrBlank()) {
                        try {
                            JSONObject(errorResponse).optString("error", null)
                        } catch (e: JSONException) {
                            null
                        }
                    } else {
                        null
                    }
                    saveData.postValue(Error(errorMessage = errorMsg))
                    return
                }
                saveData.postValue(Success())
                eventHub.dispatch(ProfileEditedEvent(newProfileData))
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                saveData.postValue(Error())
            }
        })
    }

    // cache activity state for rotation change
    fun updateProfile(newDisplayName: String, newNote: String, newLocked: Boolean, newFields: List<StringField>) {
        if (profileData.value is Success) {
            val newProfileSource = profileData.value?.data?.source?.copy(note = newNote, fields = newFields)
            val newProfile = profileData.value?.data?.copy(
                displayName = newDisplayName,
                locked = newLocked, source = newProfileSource
            )

            profileData.postValue(Success(newProfile))
        }
    }

    private fun calculateFieldToUpdate(newField: StringField?, fieldsUnchanged: Boolean): Pair<RequestBody, RequestBody>? {
        if (fieldsUnchanged || newField == null) {
            return null
        }
        return Pair(
            newField.name.toRequestBody(MultipartBody.FORM),
            newField.value.toRequestBody(MultipartBody.FORM)
        )
    }

    private fun getCacheFileForName(context: Context, filename: String): File {
        return File(context.cacheDir, filename)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, file: File): Boolean {

        val outputStream: OutputStream

        try {
            outputStream = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            Log.w(TAG, Log.getStackTraceString(e))
            return false
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        IOUtils.closeQuietly(outputStream)

        return true
    }

    override fun onCleared() {
        disposeables.dispose()
    }

    fun obtainInstance() {
        if (instanceData.value == null || instanceData.value is Error) {
            instanceData.postValue(Loading())

            flowApi.getInstance().subscribe(
                { instance ->
                    instanceData.postValue(Success(instance))
                },
                {
                    instanceData.postValue(Error())
                }
            )
                .addTo(disposeables)
        }
    }
}
