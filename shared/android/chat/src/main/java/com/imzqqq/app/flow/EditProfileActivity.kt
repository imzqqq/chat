@file:Suppress("DEPRECATION")

package com.imzqqq.app.flow

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.canhub.cropper.CropImage
import com.google.android.material.snackbar.Snackbar
import com.imzqqq.app.R
import com.imzqqq.app.flow.adapter.AccountFieldEditAdapter
import com.imzqqq.app.databinding.ActivityEditProfileBinding
import com.imzqqq.app.flow.util.Error
import com.imzqqq.app.flow.util.Loading
import com.imzqqq.app.flow.util.Resource
import com.imzqqq.app.flow.util.Success
import com.imzqqq.app.flow.util.hide
import com.imzqqq.app.flow.util.show
import com.imzqqq.app.flow.util.viewBinding
import com.imzqqq.app.flow.viewmodel.EditProfileViewModel
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.iconics.utils.colorInt
import com.mikepenz.iconics.utils.sizeDp
import dagger.hilt.android.AndroidEntryPoint
import com.imzqqq.app.core.di.VectorViewModelFactory
import com.imzqqq.app.databinding.ActivityListsBinding
import com.imzqqq.app.databinding.FragmentAccountsInListBinding
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity @Inject constructor() : BaseActivity() {

    companion object {
        const val AVATAR_SIZE = 400
        const val HEADER_WIDTH = 1500
        const val HEADER_HEIGHT = 500

        private const val AVATAR_PICK_RESULT = 1
        private const val HEADER_PICK_RESULT = 2
        private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
        private const val MAX_ACCOUNT_FIELDS = 4

        private const val BUNDLE_CURRENTLY_PICKING = "BUNDLE_CURRENTLY_PICKING"
    }

    @Inject lateinit var flowViewModelFactory: VectorViewModelFactory

    private val viewModel: EditProfileViewModel by viewModels { flowViewModelFactory }
    private lateinit var binding: ActivityEditProfileBinding

    private var currentlyPicking: PickType = PickType.NOTHING

    private val accountFieldEditAdapter = AccountFieldEditAdapter()

    private enum class PickType {
        NOTHING,
        AVATAR,
        HEADER
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.getString(BUNDLE_CURRENTLY_PICKING)?.let {
            currentlyPicking = PickType.valueOf(it)
        }

        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            setTitle(R.string.title_edit_profile)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        binding.avatarButton.setOnClickListener { onMediaPick(PickType.AVATAR) }
        binding.headerButton.setOnClickListener { onMediaPick(PickType.HEADER) }

        binding.fieldList.layoutManager = LinearLayoutManager(this)
        binding.fieldList.adapter = accountFieldEditAdapter

        val plusDrawable = IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).apply { sizeDp = 12; colorInt = Color.WHITE }

        binding.addFieldButton.setCompoundDrawablesRelativeWithIntrinsicBounds(plusDrawable, null, null, null)

        binding.addFieldButton.setOnClickListener {
            accountFieldEditAdapter.addField()
            if (accountFieldEditAdapter.itemCount >= MAX_ACCOUNT_FIELDS) {
                it.isVisible = false
            }

            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, it.bottom)
            }
        }

        viewModel.obtainProfile()

        viewModel.profileData.observe(this) { profileRes ->
            when (profileRes) {
                is Success -> {
                    val me = profileRes.data
                    if (me != null) {

                        binding.displayNameEditText.setText(me.displayName)
                        binding.noteEditText.setText(me.source?.note)
                        binding.lockedCheckBox.isChecked = me.locked

                        accountFieldEditAdapter.setFields(me.source?.fields ?: emptyList())
                        binding.addFieldButton.isEnabled = me.source?.fields?.size ?: 0 < MAX_ACCOUNT_FIELDS

                        if (viewModel.avatarData.value == null) {
                            Glide.with(this)
                                .load(me.avatar)
                                .placeholder(R.drawable.avatar_default)
                                .transform(
                                    FitCenter(),
                                    RoundedCorners(resources.getDimensionPixelSize(R.dimen.avatar_radius_80dp))
                                )
                                .into(binding.avatarPreview)
                        }

                        if (viewModel.headerData.value == null) {
                            Glide.with(this)
                                .load(me.header)
                                .into(binding.headerPreview)
                        }
                    }
                }
                is Error   -> {
                    val snackbar = Snackbar.make(binding.avatarButton, R.string.error_generic, Snackbar.LENGTH_LONG)
                    snackbar.setAction(R.string.action_retry) {
                        viewModel.obtainProfile()
                    }
                    snackbar.show()
                }
            }
        }

        viewModel.obtainInstance()
        viewModel.instanceData.observe(this) { result ->
            when (result) {
                is Success -> {
                    val instance = result.data
                    if (instance?.maxBioChars != null && instance.maxBioChars > 0) {
                        binding.noteEditTextLayout.counterMaxLength = instance.maxBioChars
                    }
                }
            }
        }

        observeImage(viewModel.avatarData, binding.avatarPreview, binding.avatarProgressBar, true)
        observeImage(viewModel.headerData, binding.headerPreview, binding.headerProgressBar, false)

        viewModel.saveData.observe(
            this,
            {
                when (it) {
                    is Success -> {
                        finish()
                    }
                    is Loading -> {
                        binding.saveProgressBar.visibility = View.VISIBLE
                    }
                    is Error   -> {
                        onSaveFailure(it.errorMessage)
                    }
                }
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(BUNDLE_CURRENTLY_PICKING, currentlyPicking.toString())
    }

    override fun onStop() {
        super.onStop()
        if (!isFinishing) {
            viewModel.updateProfile(
                binding.displayNameEditText.text.toString(),
                binding.noteEditText.text.toString(),
                binding.lockedCheckBox.isChecked,
                accountFieldEditAdapter.getFieldData()
            )
        }
    }

    private fun observeImage(
            liveData: LiveData<Resource<Bitmap>>,
            imageView: ImageView,
            progressBar: View,
            roundedCorners: Boolean
    ) {
        liveData.observe(
            this,
            {

                when (it) {
                    is Success -> {
                        val glide = Glide.with(imageView)
                            .load(it.data)

                        if (roundedCorners) {
                            glide.transform(
                                FitCenter(),
                                RoundedCorners(resources.getDimensionPixelSize(R.dimen.avatar_radius_80dp))
                            )
                        }

                        glide.into(imageView)

                        imageView.show()
                        progressBar.hide()
                    }
                    is Loading -> {
                        progressBar.show()
                    }
                    is Error   -> {
                        progressBar.hide()
                        if (!it.consumed) {
                            onResizeFailure()
                            it.consumed = true
                        }
                    }
                }
            }
        )
    }

    private fun onMediaPick(pickType: PickType) {
        if (currentlyPicking != PickType.NOTHING) {
            // Ignore inputs if another pick operation is still occurring.
            return
        }
        currentlyPicking = pickType
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            initiateMediaPicking()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initiateMediaPicking()
                } else {
                    endMediaPicking()
                    Snackbar.make(binding.avatarButton, R.string.error_media_upload_permission, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    @SuppressLint("NewApi")
    private fun initiateMediaPicking() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        when (currentlyPicking) {
            PickType.AVATAR  -> {
                startActivityForResult(intent, AVATAR_PICK_RESULT)
            }
            PickType.HEADER  -> {
                startActivityForResult(intent, HEADER_PICK_RESULT)
            }
            PickType.NOTHING -> { /* do nothing */ }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.edit_profile_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                save()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save() {
        if (currentlyPicking != PickType.NOTHING) {
            return
        }

        viewModel.save(
            binding.displayNameEditText.text.toString(),
            binding.noteEditText.text.toString(),
            binding.lockedCheckBox.isChecked,
            accountFieldEditAdapter.getFieldData(),
            this
        )
    }

    private fun onSaveFailure(msg: String?) {
        val errorMsg = msg ?: getString(R.string.error_media_upload_sending)
        Snackbar.make(binding.avatarButton, errorMsg, Snackbar.LENGTH_LONG).show()
        binding.saveProgressBar.visibility = View.GONE
    }

    private fun beginMediaPicking() {
        when (currentlyPicking) {
            PickType.AVATAR  -> {
                binding.avatarProgressBar.visibility = View.VISIBLE
                binding.avatarPreview.visibility = View.INVISIBLE
                binding.avatarButton.setImageDrawable(null)
            }
            PickType.HEADER  -> {
                binding.headerProgressBar.visibility = View.VISIBLE
                binding.headerPreview.visibility = View.INVISIBLE
                binding.headerButton.setImageDrawable(null)
            }
            PickType.NOTHING -> { /* do nothing */ }
        }
    }

    private fun endMediaPicking() {
        binding.avatarProgressBar.visibility = View.GONE
        binding.headerProgressBar.visibility = View.GONE

        currentlyPicking = PickType.NOTHING
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AVATAR_PICK_RESULT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    CropImage.activity(data.data)
                        .setInitialCropWindowPaddingRatio(0f)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setAspectRatio(AVATAR_SIZE, AVATAR_SIZE)
                        .start(this)
                } else {
                    endMediaPicking()
                }
            }
            HEADER_PICK_RESULT -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    CropImage.activity(data.data)
                        .setInitialCropWindowPaddingRatio(0f)
                        .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                        .setAspectRatio(HEADER_WIDTH, HEADER_HEIGHT)
                        .start(this)
                } else {
                    endMediaPicking()
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                when (resultCode) {
                    Activity.RESULT_OK -> beginResize(result?.uriContent)
                    CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> onResizeFailure()
                    else -> endMediaPicking()
                }
            }
        }
    }

    private fun beginResize(uri: Uri?) {
        if (uri == null) {
            currentlyPicking = PickType.NOTHING
            return
        }

        beginMediaPicking()

        when (currentlyPicking) {
            PickType.AVATAR -> {
                viewModel.newAvatar(uri, this)
            }
            PickType.HEADER -> {
                viewModel.newHeader(uri, this)
            }
            else            -> {
                throw AssertionError("PickType not set.")
            }
        }

        currentlyPicking = PickType.NOTHING
    }

    private fun onResizeFailure() {
        Snackbar.make(binding.avatarButton, R.string.error_media_upload_sending, Snackbar.LENGTH_LONG).show()
        endMediaPicking()
    }
}
