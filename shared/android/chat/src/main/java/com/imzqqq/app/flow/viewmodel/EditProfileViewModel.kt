package com.imzqqq.app.flow.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imzqqq.app.flow.EditProfileActivity.Companion.AVATAR_SIZE
import com.imzqqq.app.flow.EditProfileActivity.Companion.HEADER_HEIGHT
import com.imzqqq.app.flow.EditProfileActivity.Companion.HEADER_WIDTH
import com.imzqqq.app.flow.appstore.EventHub
import com.imzqqq.app.flow.appstore.ProfileEditedEvent
import com.imzqqq.app.flow.entity.Account
import com.imzqqq.app.flow.entity.Instance
import com.imzqqq.app.flow.entity.StringField
import com.imzqqq.app.flow.network.MastodonApi
import com.imzqqq.app.flow.util.Error
import com.imzqqq.app.flow.util.IOUtils
import com.imzqqq.app.flow.util.Loading
import com.imzqqq.app.flow.util.Resource
import com.imzqqq.app.flow.util.Success
import com.imzqqq.app.flow.util.getSampledBitmap
import com.imzqqq.app.flow.util.randomAlphanumericString
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
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

private const val HEADER_FILE_NAME = "header.png"
private const val AVATAR_FILE_NAME = "avatar.png"

private const val TAG = "EditProfileViewModel"

class EditProfileViewModel @Inject constructor(
        private val mastodonApi: MastodonApi,
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

            mastodonApi.accountVerifyCredentials()
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

        mastodonApi.accountUpdateCredentials(
            displayName, note, locked, avatar, header,
            field1?.first, field1?.second, field2?.first, field2?.second, field3?.first, field3?.second, field4?.first, field4?.second
        ).enqueue(object : Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                val newProfileData = response.body()
                if (!response.isSuccessful || newProfileData == null) {
                    val errorResponse = response.errorBody()?.string()
                    val errorMsg = if (!errorResponse.isNullOrBlank()) {
                        try {
                            // FIXME
                            // JSONObject(errorResponse).optString("error", null)
                            JSONObject(errorResponse).optString("error", "")
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
            Timber.tag(TAG).w(Log.getStackTraceString(e))
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

            mastodonApi.getInstance().subscribe(
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
