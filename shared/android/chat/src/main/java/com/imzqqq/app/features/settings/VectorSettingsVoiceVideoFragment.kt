package com.imzqqq.app.features.settings

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.preference.Preference
import androidx.preference.SwitchPreference
import com.imzqqq.app.R
import com.imzqqq.app.core.extensions.registerStartForActivityResult
import com.imzqqq.app.core.preference.VectorPreference
import com.imzqqq.app.core.utils.getCallRingtoneName
import com.imzqqq.app.core.utils.getCallRingtoneUri
import com.imzqqq.app.core.utils.setCallRingtoneUri
import com.imzqqq.app.core.utils.setUseRiotDefaultRingtone

class VectorSettingsVoiceVideoFragment : VectorSettingsBaseFragment() {

    override var titleRes = R.string.preference_voice_and_video
    override val preferenceXmlRes = R.xml.vector_settings_voice_video

    private val mUseRiotCallRingtonePreference by lazy {
        findPreference<SwitchPreference>(VectorPreferences.SETTINGS_CALL_RINGTONE_USE_RIOT_PREFERENCE_KEY)!!
    }
    private val mCallRingtonePreference by lazy {
        findPreference<VectorPreference>(VectorPreferences.SETTINGS_CALL_RINGTONE_URI_PREFERENCE_KEY)!!
    }

    override fun bindPref() {
        // Incoming call sounds
        mUseRiotCallRingtonePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            activity?.let { setUseRiotDefaultRingtone(it, mUseRiotCallRingtonePreference.isChecked) }
            false
        }

        mCallRingtonePreference.let {
            activity?.let { activity -> it.summary = getCallRingtoneName(activity) }
            it.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                displayRingtonePicker()
                false
            }
        }
    }

    private val ringtoneStartForActivityResult = registerStartForActivityResult { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val callRingtoneUri: Uri? = activityResult.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            val thisActivity = activity
            if (callRingtoneUri != null && thisActivity != null) {
                setCallRingtoneUri(thisActivity, callRingtoneUri)
                mCallRingtonePreference.summary = getCallRingtoneName(thisActivity)
            }
        }
    }

    private fun displayRingtonePicker() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.settings_call_ringtone_dialog_title))
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE)
            activity?.let { putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, getCallRingtoneUri(it)) }
        }
        ringtoneStartForActivityResult.launch(intent)
    }
}
