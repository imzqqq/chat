package com.imzqqq.app.core.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoMap
import com.imzqqq.app.features.attachments.preview.AttachmentsPreviewFragment
import com.imzqqq.app.features.contactsbook.ContactsBookFragment
import com.imzqqq.app.features.crypto.keysbackup.settings.KeysBackupSettingsFragment
import com.imzqqq.app.features.crypto.quads.SharedSecuredStorageKeyFragment
import com.imzqqq.app.features.crypto.quads.SharedSecuredStoragePassphraseFragment
import com.imzqqq.app.features.crypto.quads.SharedSecuredStorageResetAllFragment
import com.imzqqq.app.features.crypto.recover.BootstrapConclusionFragment
import com.imzqqq.app.features.crypto.recover.BootstrapConfirmPassphraseFragment
import com.imzqqq.app.features.crypto.recover.BootstrapEnterPassphraseFragment
import com.imzqqq.app.features.crypto.recover.BootstrapMigrateBackupFragment
import com.imzqqq.app.features.crypto.recover.BootstrapReAuthFragment
import com.imzqqq.app.features.crypto.recover.BootstrapSaveRecoveryKeyFragment
import com.imzqqq.app.features.crypto.recover.BootstrapSetupRecoveryKeyFragment
import com.imzqqq.app.features.crypto.recover.BootstrapWaitingFragment
import com.imzqqq.app.features.crypto.verification.QuadSLoadingFragment
import com.imzqqq.app.features.crypto.verification.cancel.VerificationCancelFragment
import com.imzqqq.app.features.crypto.verification.cancel.VerificationNotMeFragment
import com.imzqqq.app.features.crypto.verification.choose.VerificationChooseMethodFragment
import com.imzqqq.app.features.crypto.verification.conclusion.VerificationConclusionFragment
import com.imzqqq.app.features.crypto.verification.emoji.VerificationEmojiCodeFragment
import com.imzqqq.app.features.crypto.verification.qrconfirmation.VerificationQRWaitingFragment
import com.imzqqq.app.features.crypto.verification.qrconfirmation.VerificationQrScannedByOtherFragment
import com.imzqqq.app.features.crypto.verification.request.VerificationRequestFragment
import com.imzqqq.app.features.devtools.RoomDevToolEditFragment
import com.imzqqq.app.features.devtools.RoomDevToolFragment
import com.imzqqq.app.features.devtools.RoomDevToolSendFormFragment
import com.imzqqq.app.features.devtools.RoomDevToolStateEventListFragment
import com.imzqqq.app.features.discovery.DiscoverySettingsFragment
import com.imzqqq.app.features.discovery.change.SetIdentityServerFragment
import com.imzqqq.app.features.home.HomeDetailFragment
import com.imzqqq.app.features.home.HomeDrawerFragment
import com.imzqqq.app.features.home.LoadingFragment
import com.imzqqq.app.features.home.room.breadcrumbs.BreadcrumbsFragment
import com.imzqqq.app.features.home.room.detail.RoomDetailFragment
import com.imzqqq.app.features.home.room.detail.search.SearchFragment
import com.imzqqq.app.features.home.room.list.RoomListFragment
import com.imzqqq.app.features.login.LoginCaptchaFragment
import com.imzqqq.app.features.login.LoginFragment
import com.imzqqq.app.features.login.LoginGenericTextInputFormFragment
import com.imzqqq.app.features.login.LoginResetPasswordFragment
import com.imzqqq.app.features.login.LoginResetPasswordMailConfirmationFragment
import com.imzqqq.app.features.login.LoginResetPasswordSuccessFragment
import com.imzqqq.app.features.login.LoginServerSelectionFragment
import com.imzqqq.app.features.login.LoginServerUrlFormFragment
import com.imzqqq.app.features.login.LoginSignUpSignInSelectionFragment
import com.imzqqq.app.features.login.LoginSplashFragment
import com.imzqqq.app.features.login.LoginWaitForEmailFragment
import com.imzqqq.app.features.login.LoginWebFragment
import com.imzqqq.app.features.login.terms.LoginTermsFragment
import com.imzqqq.app.features.login2.LoginCaptchaFragment2
import com.imzqqq.app.features.login2.LoginFragmentSigninPassword2
import com.imzqqq.app.features.login2.LoginFragmentSigninUsername2
import com.imzqqq.app.features.login2.LoginFragmentSignupPassword2
import com.imzqqq.app.features.login2.LoginFragmentSignupUsername2
import com.imzqqq.app.features.login2.LoginFragmentToAny2
import com.imzqqq.app.features.login2.LoginGenericTextInputFormFragment2
import com.imzqqq.app.features.login2.LoginResetPasswordFragment2
import com.imzqqq.app.features.login2.LoginResetPasswordMailConfirmationFragment2
import com.imzqqq.app.features.login2.LoginResetPasswordSuccessFragment2
import com.imzqqq.app.features.login2.LoginServerSelectionFragment2
import com.imzqqq.app.features.login2.LoginServerUrlFormFragment2
import com.imzqqq.app.features.login2.LoginSplashSignUpSignInSelectionFragment2
import com.imzqqq.app.features.login2.LoginSsoOnlyFragment2
import com.imzqqq.app.features.login2.LoginWaitForEmailFragment2
import com.imzqqq.app.features.login2.LoginWebFragment2
import com.imzqqq.app.features.login2.created.AccountCreatedFragment
import com.imzqqq.app.features.login2.terms.LoginTermsFragment2
import com.imzqqq.app.features.matrixto.MatrixToRoomSpaceFragment
import com.imzqqq.app.features.matrixto.MatrixToUserFragment
import com.imzqqq.app.features.pin.PinFragment
import com.imzqqq.app.features.poll.create.CreatePollFragment
import com.imzqqq.app.features.qrcode.QrCodeScannerFragment
import com.imzqqq.app.features.reactions.EmojiChooserFragment
import com.imzqqq.app.features.reactions.EmojiSearchResultFragment
import com.imzqqq.app.features.roomdirectory.PublicRoomsFragment
import com.imzqqq.app.features.roomdirectory.createroom.CreateRoomFragment
import com.imzqqq.app.features.roomdirectory.picker.RoomDirectoryPickerFragment
import com.imzqqq.app.features.roomdirectory.roompreview.RoomPreviewNoPreviewFragment
import com.imzqqq.app.features.roommemberprofile.RoomMemberProfileFragment
import com.imzqqq.app.features.roommemberprofile.devices.DeviceListFragment
import com.imzqqq.app.features.roommemberprofile.devices.DeviceTrustInfoActionFragment
import com.imzqqq.app.features.roomprofile.RoomProfileFragment
import com.imzqqq.app.features.roomprofile.alias.RoomAliasFragment
import com.imzqqq.app.features.roomprofile.banned.RoomBannedMemberListFragment
import com.imzqqq.app.features.roomprofile.members.RoomMemberListFragment
import com.imzqqq.app.features.roomprofile.notifications.RoomNotificationSettingsFragment
import com.imzqqq.app.features.roomprofile.permissions.RoomPermissionsFragment
import com.imzqqq.app.features.roomprofile.settings.RoomSettingsFragment
import com.imzqqq.app.features.roomprofile.settings.joinrule.RoomJoinRuleFragment
import com.imzqqq.app.features.roomprofile.settings.joinrule.advanced.RoomJoinRuleChooseRestrictedFragment
import com.imzqqq.app.features.roomprofile.uploads.RoomUploadsFragment
import com.imzqqq.app.features.roomprofile.uploads.files.RoomUploadsFilesFragment
import com.imzqqq.app.features.roomprofile.uploads.media.RoomUploadsMediaFragment
import com.imzqqq.app.features.settings.VectorSettingsAdvancedSettingsFragment
import com.imzqqq.app.features.settings.VectorSettingsGeneralFragment
import com.imzqqq.app.features.settings.VectorSettingsHelpAboutFragment
import com.imzqqq.app.features.settings.VectorSettingsLabsFragment
import com.imzqqq.app.features.settings.VectorSettingsPinFragment
import com.imzqqq.app.features.settings.VectorSettingsPreferencesFragment
import com.imzqqq.app.features.settings.VectorSettingsSecurityPrivacyFragment
import com.imzqqq.app.features.settings.account.deactivation.DeactivateAccountFragment
import com.imzqqq.app.features.settings.crosssigning.CrossSigningSettingsFragment
import com.imzqqq.app.features.settings.devices.VectorSettingsDevicesFragment
import com.imzqqq.app.features.settings.devtools.AccountDataFragment
import com.imzqqq.app.features.settings.devtools.GossipingEventsPaperTrailFragment
import com.imzqqq.app.features.settings.devtools.IncomingKeyRequestListFragment
import com.imzqqq.app.features.settings.devtools.KeyRequestsFragment
import com.imzqqq.app.features.settings.devtools.OutgoingKeyRequestListFragment
import com.imzqqq.app.features.settings.homeserver.HomeserverSettingsFragment
import com.imzqqq.app.features.settings.ignored.VectorSettingsIgnoredUsersFragment
import com.imzqqq.app.features.settings.locale.LocalePickerFragment
import com.imzqqq.app.features.settings.notifications.VectorSettingsAdvancedNotificationPreferenceFragment
import com.imzqqq.app.features.settings.notifications.VectorSettingsNotificationPreferenceFragment
import com.imzqqq.app.features.settings.notifications.VectorSettingsNotificationsTroubleshootFragment
import com.imzqqq.app.features.settings.push.PushGatewaysFragment
import com.imzqqq.app.features.settings.push.PushRulesFragment
import com.imzqqq.app.features.settings.threepids.ThreePidsSettingsFragment
import com.imzqqq.app.features.share.IncomingShareFragment
import com.imzqqq.app.features.signout.soft.SoftLogoutFragment
import com.imzqqq.app.features.spaces.SpaceListFragment
import com.imzqqq.app.features.spaces.create.ChoosePrivateSpaceTypeFragment
import com.imzqqq.app.features.spaces.create.ChooseSpaceTypeFragment
import com.imzqqq.app.features.spaces.create.CreateSpaceAdd3pidInvitesFragment
import com.imzqqq.app.features.spaces.create.CreateSpaceDefaultRoomsFragment
import com.imzqqq.app.features.spaces.create.CreateSpaceDetailsFragment
import com.imzqqq.app.features.spaces.explore.SpaceDirectoryFragment
import com.imzqqq.app.features.spaces.leave.SpaceLeaveAdvancedFragment
import com.imzqqq.app.features.spaces.manage.SpaceAddRoomFragment
import com.imzqqq.app.features.spaces.manage.SpaceManageRoomsFragment
import com.imzqqq.app.features.spaces.manage.SpaceSettingsFragment
import com.imzqqq.app.features.spaces.people.SpacePeopleFragment
import com.imzqqq.app.features.spaces.preview.SpacePreviewFragment
import com.imzqqq.app.features.terms.ReviewTermsFragment
import com.imzqqq.app.features.usercode.ShowUserCodeFragment
import com.imzqqq.app.features.userdirectory.UserListFragment
import com.imzqqq.app.features.widgets.WidgetFragment

@InstallIn(ActivityComponent::class)
@Module
interface FragmentModule {

    /**
     * Fragments with @IntoMap will be injected by this factory
     */
    @Binds
    fun bindFragmentFactory(factory: VectorFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(RoomListFragment::class)
    fun bindRoomListFragment(fragment: RoomListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LocalePickerFragment::class)
    fun bindLocalePickerFragment(fragment: LocalePickerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceListFragment::class)
    fun bindSpaceListFragment(fragment: SpaceListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDetailFragment::class)
    fun bindRoomDetailFragment(fragment: RoomDetailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDirectoryPickerFragment::class)
    fun bindRoomDirectoryPickerFragment(fragment: RoomDirectoryPickerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CreateRoomFragment::class)
    fun bindCreateRoomFragment(fragment: CreateRoomFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomPreviewNoPreviewFragment::class)
    fun bindRoomPreviewNoPreviewFragment(fragment: RoomPreviewNoPreviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(KeysBackupSettingsFragment::class)
    fun bindKeysBackupSettingsFragment(fragment: KeysBackupSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoadingFragment::class)
    fun bindLoadingFragment(fragment: LoadingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(HomeDrawerFragment::class)
    fun bindHomeDrawerFragment(fragment: HomeDrawerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(HomeDetailFragment::class)
    fun bindHomeDetailFragment(fragment: HomeDetailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(EmojiSearchResultFragment::class)
    fun bindEmojiSearchResultFragment(fragment: EmojiSearchResultFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragment::class)
    fun bindLoginFragment(fragment: LoginFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginCaptchaFragment::class)
    fun bindLoginCaptchaFragment(fragment: LoginCaptchaFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginTermsFragment::class)
    fun bindLoginTermsFragment(fragment: LoginTermsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginServerUrlFormFragment::class)
    fun bindLoginServerUrlFormFragment(fragment: LoginServerUrlFormFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordMailConfirmationFragment::class)
    fun bindLoginResetPasswordMailConfirmationFragment(fragment: LoginResetPasswordMailConfirmationFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordFragment::class)
    fun bindLoginResetPasswordFragment(fragment: LoginResetPasswordFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordSuccessFragment::class)
    fun bindLoginResetPasswordSuccessFragment(fragment: LoginResetPasswordSuccessFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginServerSelectionFragment::class)
    fun bindLoginServerSelectionFragment(fragment: LoginServerSelectionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginSignUpSignInSelectionFragment::class)
    fun bindLoginSignUpSignInSelectionFragment(fragment: LoginSignUpSignInSelectionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginSplashFragment::class)
    fun bindLoginSplashFragment(fragment: LoginSplashFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginWebFragment::class)
    fun bindLoginWebFragment(fragment: LoginWebFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginGenericTextInputFormFragment::class)
    fun bindLoginGenericTextInputFormFragment(fragment: LoginGenericTextInputFormFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginWaitForEmailFragment::class)
    fun bindLoginWaitForEmailFragment(fragment: LoginWaitForEmailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragmentSigninUsername2::class)
    fun bindLoginFragmentSigninUsername2(fragment: LoginFragmentSigninUsername2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AccountCreatedFragment::class)
    fun bindAccountCreatedFragment(fragment: AccountCreatedFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragmentSignupUsername2::class)
    fun bindLoginFragmentSignupUsername2(fragment: LoginFragmentSignupUsername2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragmentSigninPassword2::class)
    fun bindLoginFragmentSigninPassword2(fragment: LoginFragmentSigninPassword2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragmentSignupPassword2::class)
    fun bindLoginFragmentSignupPassword2(fragment: LoginFragmentSignupPassword2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginCaptchaFragment2::class)
    fun bindLoginCaptchaFragment2(fragment: LoginCaptchaFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginFragmentToAny2::class)
    fun bindLoginFragmentToAny2(fragment: LoginFragmentToAny2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginTermsFragment2::class)
    fun bindLoginTermsFragment2(fragment: LoginTermsFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginServerUrlFormFragment2::class)
    fun bindLoginServerUrlFormFragment2(fragment: LoginServerUrlFormFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordMailConfirmationFragment2::class)
    fun bindLoginResetPasswordMailConfirmationFragment2(fragment: LoginResetPasswordMailConfirmationFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordFragment2::class)
    fun bindLoginResetPasswordFragment2(fragment: LoginResetPasswordFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginResetPasswordSuccessFragment2::class)
    fun bindLoginResetPasswordSuccessFragment2(fragment: LoginResetPasswordSuccessFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginServerSelectionFragment2::class)
    fun bindLoginServerSelectionFragment2(fragment: LoginServerSelectionFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginSsoOnlyFragment2::class)
    fun bindLoginSsoOnlyFragment2(fragment: LoginSsoOnlyFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginSplashSignUpSignInSelectionFragment2::class)
    fun bindLoginSplashSignUpSignInSelectionFragment2(fragment: LoginSplashSignUpSignInSelectionFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginWebFragment2::class)
    fun bindLoginWebFragment2(fragment: LoginWebFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginGenericTextInputFormFragment2::class)
    fun bindLoginGenericTextInputFormFragment2(fragment: LoginGenericTextInputFormFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LoginWaitForEmailFragment2::class)
    fun bindLoginWaitForEmailFragment2(fragment: LoginWaitForEmailFragment2): Fragment

    @Binds
    @IntoMap
    @FragmentKey(UserListFragment::class)
    fun bindUserListFragment(fragment: UserListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PushGatewaysFragment::class)
    fun bindPushGatewaysFragment(fragment: PushGatewaysFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsNotificationsTroubleshootFragment::class)
    fun bindVectorSettingsNotificationsTroubleshootFragment(fragment: VectorSettingsNotificationsTroubleshootFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsAdvancedNotificationPreferenceFragment::class)
    fun bindVectorSettingsAdvancedNotificationPreferenceFragment(fragment: VectorSettingsAdvancedNotificationPreferenceFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsNotificationPreferenceFragment::class)
    fun bindVectorSettingsNotificationPreferenceFragment(fragment: VectorSettingsNotificationPreferenceFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsLabsFragment::class)
    fun bindVectorSettingsLabsFragment(fragment: VectorSettingsLabsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(HomeserverSettingsFragment::class)
    fun bindHomeserverSettingsFragment(fragment: HomeserverSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsPinFragment::class)
    fun bindVectorSettingsPinFragment(fragment: VectorSettingsPinFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsGeneralFragment::class)
    fun bindVectorSettingsGeneralFragment(fragment: VectorSettingsGeneralFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PushRulesFragment::class)
    fun bindPushRulesFragment(fragment: PushRulesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsPreferencesFragment::class)
    fun bindVectorSettingsPreferencesFragment(fragment: VectorSettingsPreferencesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsSecurityPrivacyFragment::class)
    fun bindVectorSettingsSecurityPrivacyFragment(fragment: VectorSettingsSecurityPrivacyFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsHelpAboutFragment::class)
    fun bindVectorSettingsHelpAboutFragment(fragment: VectorSettingsHelpAboutFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsIgnoredUsersFragment::class)
    fun bindVectorSettingsIgnoredUsersFragment(fragment: VectorSettingsIgnoredUsersFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsDevicesFragment::class)
    fun bindVectorSettingsDevicesFragment(fragment: VectorSettingsDevicesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VectorSettingsAdvancedSettingsFragment::class)
    fun bindVectorSettingsAdvancedSettingsFragment(fragment: VectorSettingsAdvancedSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ThreePidsSettingsFragment::class)
    fun bindThreePidsSettingsFragment(fragment: ThreePidsSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PublicRoomsFragment::class)
    fun bindPublicRoomsFragment(fragment: PublicRoomsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomProfileFragment::class)
    fun bindRoomProfileFragment(fragment: RoomProfileFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomMemberListFragment::class)
    fun bindRoomMemberListFragment(fragment: RoomMemberListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomUploadsFragment::class)
    fun bindRoomUploadsFragment(fragment: RoomUploadsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomUploadsMediaFragment::class)
    fun bindRoomUploadsMediaFragment(fragment: RoomUploadsMediaFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomUploadsFilesFragment::class)
    fun bindRoomUploadsFilesFragment(fragment: RoomUploadsFilesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomSettingsFragment::class)
    fun bindRoomSettingsFragment(fragment: RoomSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomAliasFragment::class)
    fun bindRoomAliasFragment(fragment: RoomAliasFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomPermissionsFragment::class)
    fun bindRoomPermissionsFragment(fragment: RoomPermissionsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomMemberProfileFragment::class)
    fun bindRoomMemberProfileFragment(fragment: RoomMemberProfileFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BreadcrumbsFragment::class)
    fun bindBreadcrumbsFragment(fragment: BreadcrumbsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(EmojiChooserFragment::class)
    fun bindEmojiChooserFragment(fragment: EmojiChooserFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SoftLogoutFragment::class)
    fun bindSoftLogoutFragment(fragment: SoftLogoutFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationRequestFragment::class)
    fun bindVerificationRequestFragment(fragment: VerificationRequestFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationChooseMethodFragment::class)
    fun bindVerificationChooseMethodFragment(fragment: VerificationChooseMethodFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationEmojiCodeFragment::class)
    fun bindVerificationEmojiCodeFragment(fragment: VerificationEmojiCodeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationQrScannedByOtherFragment::class)
    fun bindVerificationQrScannedByOtherFragment(fragment: VerificationQrScannedByOtherFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationQRWaitingFragment::class)
    fun bindVerificationQRWaitingFragment(fragment: VerificationQRWaitingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationConclusionFragment::class)
    fun bindVerificationConclusionFragment(fragment: VerificationConclusionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationCancelFragment::class)
    fun bindVerificationCancelFragment(fragment: VerificationCancelFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(QuadSLoadingFragment::class)
    fun bindQuadSLoadingFragment(fragment: QuadSLoadingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(VerificationNotMeFragment::class)
    fun bindVerificationNotMeFragment(fragment: VerificationNotMeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(QrCodeScannerFragment::class)
    fun bindQrCodeScannerFragment(fragment: QrCodeScannerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DeviceListFragment::class)
    fun bindDeviceListFragment(fragment: DeviceListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DeviceTrustInfoActionFragment::class)
    fun bindDeviceTrustInfoActionFragment(fragment: DeviceTrustInfoActionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CrossSigningSettingsFragment::class)
    fun bindCrossSigningSettingsFragment(fragment: CrossSigningSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AttachmentsPreviewFragment::class)
    fun bindAttachmentsPreviewFragment(fragment: AttachmentsPreviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(IncomingShareFragment::class)
    fun bindIncomingShareFragment(fragment: IncomingShareFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(AccountDataFragment::class)
    fun bindAccountDataFragment(fragment: AccountDataFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(OutgoingKeyRequestListFragment::class)
    fun bindOutgoingKeyRequestListFragment(fragment: OutgoingKeyRequestListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(IncomingKeyRequestListFragment::class)
    fun bindIncomingKeyRequestListFragment(fragment: IncomingKeyRequestListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(KeyRequestsFragment::class)
    fun bindKeyRequestsFragment(fragment: KeyRequestsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(GossipingEventsPaperTrailFragment::class)
    fun bindGossipingEventsPaperTrailFragment(fragment: GossipingEventsPaperTrailFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapEnterPassphraseFragment::class)
    fun bindBootstrapEnterPassphraseFragment(fragment: BootstrapEnterPassphraseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapConfirmPassphraseFragment::class)
    fun bindBootstrapConfirmPassphraseFragment(fragment: BootstrapConfirmPassphraseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapWaitingFragment::class)
    fun bindBootstrapWaitingFragment(fragment: BootstrapWaitingFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapSetupRecoveryKeyFragment::class)
    fun bindBootstrapSetupRecoveryKeyFragment(fragment: BootstrapSetupRecoveryKeyFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapSaveRecoveryKeyFragment::class)
    fun bindBootstrapSaveRecoveryKeyFragment(fragment: BootstrapSaveRecoveryKeyFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapConclusionFragment::class)
    fun bindBootstrapConclusionFragment(fragment: BootstrapConclusionFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapReAuthFragment::class)
    fun bindBootstrapReAuthFragment(fragment: BootstrapReAuthFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(BootstrapMigrateBackupFragment::class)
    fun bindBootstrapMigrateBackupFragment(fragment: BootstrapMigrateBackupFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DeactivateAccountFragment::class)
    fun bindDeactivateAccountFragment(fragment: DeactivateAccountFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SharedSecuredStoragePassphraseFragment::class)
    fun bindSharedSecuredStoragePassphraseFragment(fragment: SharedSecuredStoragePassphraseFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SharedSecuredStorageKeyFragment::class)
    fun bindSharedSecuredStorageKeyFragment(fragment: SharedSecuredStorageKeyFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SharedSecuredStorageResetAllFragment::class)
    fun bindSharedSecuredStorageResetAllFragment(fragment: SharedSecuredStorageResetAllFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SetIdentityServerFragment::class)
    fun bindSetIdentityServerFragment(fragment: SetIdentityServerFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(DiscoverySettingsFragment::class)
    fun bindDiscoverySettingsFragment(fragment: DiscoverySettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ReviewTermsFragment::class)
    fun bindReviewTermsFragment(fragment: ReviewTermsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(WidgetFragment::class)
    fun bindWidgetFragment(fragment: WidgetFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ContactsBookFragment::class)
    fun bindPhoneBookFragment(fragment: ContactsBookFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(PinFragment::class)
    fun bindPinFragment(fragment: PinFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomBannedMemberListFragment::class)
    fun bindRoomBannedMemberListFragment(fragment: RoomBannedMemberListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomNotificationSettingsFragment::class)
    fun bindRoomNotificationSettingsFragment(fragment: RoomNotificationSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SearchFragment::class)
    fun bindSearchFragment(fragment: SearchFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ShowUserCodeFragment::class)
    fun bindShowUserCodeFragment(fragment: ShowUserCodeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDevToolFragment::class)
    fun bindRoomDevToolFragment(fragment: RoomDevToolFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDevToolStateEventListFragment::class)
    fun bindRoomDevToolStateEventListFragment(fragment: RoomDevToolStateEventListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDevToolEditFragment::class)
    fun bindRoomDevToolEditFragment(fragment: RoomDevToolEditFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomDevToolSendFormFragment::class)
    fun bindRoomDevToolSendFormFragment(fragment: RoomDevToolSendFormFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpacePreviewFragment::class)
    fun bindSpacePreviewFragment(fragment: SpacePreviewFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ChooseSpaceTypeFragment::class)
    fun bindChooseSpaceTypeFragment(fragment: ChooseSpaceTypeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CreateSpaceDetailsFragment::class)
    fun bindCreateSpaceDetailsFragment(fragment: CreateSpaceDetailsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CreateSpaceDefaultRoomsFragment::class)
    fun bindCreateSpaceDefaultRoomsFragment(fragment: CreateSpaceDefaultRoomsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MatrixToUserFragment::class)
    fun bindMatrixToUserFragment(fragment: MatrixToUserFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(MatrixToRoomSpaceFragment::class)
    fun bindMatrixToRoomSpaceFragment(fragment: MatrixToRoomSpaceFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceDirectoryFragment::class)
    fun bindSpaceDirectoryFragment(fragment: SpaceDirectoryFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ChoosePrivateSpaceTypeFragment::class)
    fun bindChoosePrivateSpaceTypeFragment(fragment: ChoosePrivateSpaceTypeFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CreateSpaceAdd3pidInvitesFragment::class)
    fun bindCreateSpaceAdd3pidInvitesFragment(fragment: CreateSpaceAdd3pidInvitesFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceAddRoomFragment::class)
    fun bindSpaceAddRoomFragment(fragment: SpaceAddRoomFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpacePeopleFragment::class)
    fun bindSpacePeopleFragment(fragment: SpacePeopleFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceSettingsFragment::class)
    fun bindSpaceSettingsFragment(fragment: SpaceSettingsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceManageRoomsFragment::class)
    fun bindSpaceManageRoomsFragment(fragment: SpaceManageRoomsFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomJoinRuleFragment::class)
    fun bindRoomJoinRuleFragment(fragment: RoomJoinRuleFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(RoomJoinRuleChooseRestrictedFragment::class)
    fun bindRoomJoinRuleChooseRestrictedFragment(fragment: RoomJoinRuleChooseRestrictedFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SpaceLeaveAdvancedFragment::class)
    fun bindSpaceLeaveAdvancedFragment(fragment: SpaceLeaveAdvancedFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(CreatePollFragment::class)
    fun bindCreatePollFragment(fragment: CreatePollFragment): Fragment
}
