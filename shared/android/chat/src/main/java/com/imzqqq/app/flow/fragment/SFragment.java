package com.imzqqq.app.flow.fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import com.imzqqq.app.R;
import com.imzqqq.app.flow.BaseActivity;
import com.imzqqq.app.flow.BottomSheetActivity;
import com.imzqqq.app.flow.FlowActivity;
import com.imzqqq.app.flow.PostLookupFallbackBehavior;
import com.imzqqq.app.flow.ViewMediaActivity;
import com.imzqqq.app.flow.ViewTagActivity;
import com.imzqqq.app.flow.components.compose.ComposeActivity;
import com.imzqqq.app.flow.components.compose.ComposeActivity.ComposeOptions;
import com.imzqqq.app.flow.components.report.ReportActivity;
import com.imzqqq.app.flow.db.AccountEntity;
import com.imzqqq.app.flow.db.AccountManager;
import com.imzqqq.app.flow.entity.Attachment;
import com.imzqqq.app.flow.entity.Status;
import com.imzqqq.app.flow.network.MastodonApi;
import com.imzqqq.app.flow.network.TimelineCases;
import com.imzqqq.app.flow.util.LinkHelper;
import com.imzqqq.app.flow.view.MuteAccountDialog;
import com.imzqqq.app.flow.viewdata.AttachmentViewData;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import kotlin.Unit;
import timber.log.Timber;

import static autodispose2.AutoDispose.autoDisposable;
import static autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider.from;

/* Note from Andrew on Jan. 22, 2017: This class is a design problem for me, so I left it with an
 * awkward name. TimelineFragment and NotificationFragment have significant overlap but the nature
 * of that is complicated by how they're coupled with Status and Notification and the corresponding
 * adapters. I feel like the profile pages and thread viewer, which I haven't made yet, will also
 * overlap functionality. So, I'm momentarily leaving it and hopefully working on those will clear
 * up what needs to be where. */
public abstract class SFragment extends Fragment {

    @Inject public MastodonApi mastodonApi;
    @Inject public AccountManager accountManager;
    @Inject public TimelineCases timelineCases;

    protected abstract void removeItem(int position);

    protected abstract void onReblog(final boolean reblog, final int position);

    private BottomSheetActivity bottomSheetActivity;

    private static final String TAG = "SFragment";

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomSheetActivity) {
            bottomSheetActivity = (BottomSheetActivity) context;
        } else {
            throw new IllegalStateException("Fragment must be attached to a BottomSheetActivity!");
        }
    }

    protected void openReblog(@Nullable final Status status) {
        if (status == null) return;
        bottomSheetActivity.viewAccount(status.getAccount().getId());
    }

    protected void viewThread(String statusId, @Nullable String statusUrl) {
        bottomSheetActivity.viewThread(statusId, statusUrl);
    }

    protected void viewAccount(String accountId) {
        bottomSheetActivity.viewAccount(accountId);
    }

    public void onViewUrl(String url) {
        bottomSheetActivity.viewUrl(url, PostLookupFallbackBehavior.OPEN_IN_BROWSER);
    }

    protected void reply(Status status) {
        String inReplyToId = status.getActionableId();
        Status actionableStatus = status.getActionableStatus();
        Status.Visibility replyVisibility = actionableStatus.getVisibility();
        String contentWarning = actionableStatus.getSpoilerText();
        List<Status.Mention> mentions = actionableStatus.getMentions();
        Set<String> mentionedUsernames = new LinkedHashSet<>();
        mentionedUsernames.add(actionableStatus.getAccount().getUsername());
        String loggedInUsername = null;
        AccountEntity activeAccount = accountManager.getActiveAccount();
        if (activeAccount != null) {
            loggedInUsername = activeAccount.getUsername();
        }
        for (Status.Mention mention : mentions) {
            mentionedUsernames.add(mention.getUsername());
        }
        mentionedUsernames.remove(loggedInUsername);
        ComposeOptions composeOptions = new ComposeOptions();
        composeOptions.setInReplyToId(inReplyToId);
        composeOptions.setReplyVisibility(replyVisibility);
        composeOptions.setContentWarning(contentWarning);
        composeOptions.setMentionedUsernames(mentionedUsernames);
        composeOptions.setReplyingStatusAuthor(actionableStatus.getAccount().getLocalUsername());
        composeOptions.setReplyingStatusContent(actionableStatus.getContent().toString());

        Intent intent = ComposeActivity.startIntent(getContext(), composeOptions);
        getActivity().startActivity(intent);
    }

    protected void more(@NonNull final Status status, View view, final int position) {
        final String id = status.getActionableId();
        final String accountId = status.getActionableStatus().getAccount().getId();
        final String accountUsername = status.getActionableStatus().getAccount().getUsername();
        final String statusUrl = status.getActionableStatus().getUrl();
        List<AccountEntity> accounts = accountManager.getAllAccountsOrderedByActive();
        String openAsTitle = null;

        String loggedInAccountId = null;
        AccountEntity activeAccount = accountManager.getActiveAccount();
        if (activeAccount != null) {
            loggedInAccountId = activeAccount.getAccountId();
        }

        PopupMenu popup = new PopupMenu(getContext(), view);
        // Give a different menu depending on whether this is the user's own toot or not.
        boolean statusIsByCurrentUser = loggedInAccountId != null && loggedInAccountId.equals(accountId);
        if (statusIsByCurrentUser) {
            popup.inflate(R.menu.status_more_for_user);
            Menu menu = popup.getMenu();
            switch (status.getVisibility()) {
                case PUBLIC:
                case UNLISTED: {
                    final String textId =
                            getString(status.isPinned() ? R.string.unpin_action : R.string.pin_action);
                    menu.add(0, R.id.pin, 1, textId);
                    break;
                }
                case PRIVATE: {
                    boolean reblogged = status.getReblogged();
                    if (status.getReblog() != null) reblogged = status.getReblog().getReblogged();
                    menu.findItem(R.id.status_reblog_private).setVisible(!reblogged);
                    menu.findItem(R.id.status_unreblog_private).setVisible(reblogged);
                    break;
                }
            }
        } else {
            popup.inflate(R.menu.status_more);
            Menu menu = popup.getMenu();
            menu.findItem(R.id.status_download_media).setVisible(!status.getAttachments().isEmpty());
        }

        Menu menu = popup.getMenu();
        MenuItem openAsItem = menu.findItem(R.id.status_open_as);
        switch (accounts.size()) {
            case 0:
            case 1:
                openAsItem.setVisible(false);
                break;
            case 2:
                for (AccountEntity account : accounts) {
                    if (account != activeAccount) {
                        openAsTitle = String.format(getString(R.string.action_open_as), account.getFullName());
                        break;
                    }
                }
                break;
            default:
                openAsTitle = String.format(getString(R.string.action_open_as), "…");
                break;
        }
        openAsItem.setTitle(openAsTitle);

        MenuItem muteConversationItem = menu.findItem(R.id.status_mute_conversation);
        boolean mutable = statusIsByCurrentUser || accountIsInMentions(activeAccount, status.getMentions());
        muteConversationItem.setVisible(mutable);
        if (mutable) {
            muteConversationItem.setTitle((status.getMuted() == null || !status.getMuted()) ?
                    R.string.action_mute_conversation :
                    R.string.action_unmute_conversation);
        }

        popup.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.status_share_content) {
                Status statusToShare = status;
                if (statusToShare.getReblog() != null)
                    statusToShare = statusToShare.getReblog();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String stringToShare = statusToShare.getAccount().getUsername() +
                        " - " +
                        statusToShare.getContent().toString();
                sendIntent.putExtra(Intent.EXTRA_TEXT, stringToShare);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, statusUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_status_content_to)));
                return true;
            } else if (itemId == R.id.status_share_link) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, statusUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_status_link_to)));
                return true;
            } else if (itemId == R.id.status_copy_link) {
                ClipboardManager clipboard = (ClipboardManager)
                        getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(null, statusUrl);
                clipboard.setPrimaryClip(clip);
                return true;
            } else if (itemId == R.id.status_open_as) {
                showOpenAsDialog(statusUrl, item.getTitle());
                return true;
            } else if (itemId == R.id.status_download_media) {
                requestDownloadAllMedia(status);
                return true;
            } else if (itemId == R.id.status_mute) {
                onMute(accountId, accountUsername);
                return true;
            } else if (itemId == R.id.status_block) {
                onBlock(accountId, accountUsername);
                return true;
            } else if (itemId == R.id.status_report) {
                openReportPage(accountId, accountUsername, id);
                return true;
            } else if (itemId == R.id.status_unreblog_private) {
                onReblog(false, position);
                return true;
            } else if (itemId == R.id.status_reblog_private) {
                onReblog(true, position);
                return true;
            } else if (itemId == R.id.status_delete) {
                showConfirmDeleteDialog(id, position);
                return true;
            } else if (itemId == R.id.status_delete_and_redraft) {
                showConfirmEditDialog(id, position, status);
                return true;
            } else if (itemId == R.id.pin) {
                timelineCases.pin(status.getId(), !status.isPinned());
                return true;
            } else if (itemId == R.id.status_mute_conversation) {
                timelineCases.muteConversation(status.getId(), status.getMuted() == null || !status.getMuted())
                        .onErrorReturnItem(status)
                        .observeOn(AndroidSchedulers.mainThread())
                        .to(autoDisposable(from(this, Lifecycle.Event.ON_DESTROY)))
                        .subscribe();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void onMute(String accountId, String accountUsername) {
        MuteAccountDialog.showMuteAccountDialog(
                this.getActivity(),
                accountUsername,
                (notifications, duration) -> {
                    timelineCases.mute(accountId, notifications, duration);
                    return Unit.INSTANCE;
                }
        );
    }

    private void onBlock(String accountId, String accountUsername) {
        new AlertDialog.Builder(requireContext())
                .setMessage(getString(R.string.dialog_block_warning, accountUsername))
                .setPositiveButton(android.R.string.ok, (__, ___) -> timelineCases.block(accountId))
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private static boolean accountIsInMentions(AccountEntity account, List<Status.Mention> mentions) {
        if (account == null) {
            return false;
        }

        for (Status.Mention mention : mentions) {
            if (account.getUsername().equals(mention.getUsername())) {
                Uri uri = Uri.parse(mention.getUrl());
                if (uri != null && account.getDomain().equals(uri.getHost())) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void viewMedia(int urlIndex, List<AttachmentViewData> attachments, @Nullable View view) {
        final AttachmentViewData active = attachments.get(urlIndex);
        Attachment.Type type = active.getAttachment().getType();
        switch (type) {
            case GIFV:
            case VIDEO:
            case IMAGE:
            case AUDIO: {
                final Intent intent = ViewMediaActivity.newIntent(getContext(), attachments,
                        urlIndex);
                if (view != null) {
                    String url = active.getAttachment().getUrl();
                    ViewCompat.setTransitionName(view, url);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                    view, url);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
                break;
            }
            default:
            case UNKNOWN: {
                LinkHelper.openLink(active.getAttachment().getUrl(), getContext());
                break;
            }
        }
    }

    protected void viewTag(String tag) {
        Intent intent = new Intent(getContext(), ViewTagActivity.class);
        intent.putExtra("hashtag", tag);
        startActivity(intent);
    }

    protected void openReportPage(String accountId, String accountUsername, String statusId) {
        startActivity(ReportActivity.getIntent(requireContext(), accountId, accountUsername, statusId));
    }

    protected void showConfirmDeleteDialog(final String id, final int position) {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_delete_toot_warning)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    timelineCases.delete(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .to(autoDisposable(from(this, Lifecycle.Event.ON_DESTROY)))
                            .subscribe(
                                    deletedStatus -> {
                                    },
                                    error -> {
                                        Timber.tag("SFragment").w(error, "error deleting status");
                                        Toast.makeText(getContext(), R.string.error_generic, Toast.LENGTH_SHORT).show();
                                    });
                    removeItem(position);
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showConfirmEditDialog(final String id, final int position, final Status status) {
        if (getActivity() == null) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.dialog_redraft_toot_warning)
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    timelineCases.delete(id)
                            .observeOn(AndroidSchedulers.mainThread())
                            .to(autoDisposable(from(this, Lifecycle.Event.ON_DESTROY)))
                            .subscribe(deletedStatus -> {
                                        removeItem(position);

                                        if (deletedStatus.isEmpty()) {
                                            deletedStatus = status.toDeletedStatus();
                                        }
                                        ComposeOptions composeOptions = new ComposeOptions();
                                        composeOptions.setTootText(deletedStatus.getText());
                                        composeOptions.setInReplyToId(deletedStatus.getInReplyToId());
                                        composeOptions.setVisibility(deletedStatus.getVisibility());
                                        composeOptions.setContentWarning(deletedStatus.getSpoilerText());
                                        composeOptions.setMediaAttachments(deletedStatus.getAttachments());
                                        composeOptions.setSensitive(deletedStatus.getSensitive());
                                        composeOptions.setModifiedInitialState(true);
                                        if (deletedStatus.getPoll() != null) {
                                            composeOptions.setPoll(deletedStatus.getPoll().toNewPoll(deletedStatus.getCreatedAt()));
                                        }

                                        Intent intent = ComposeActivity
                                                .startIntent(getContext(), composeOptions);
                                        startActivity(intent);
                                    },
                                    error -> {
                                        Timber.tag("SFragment").w(error, "error deleting status");
                                        Toast.makeText(getContext(), R.string.error_generic, Toast.LENGTH_SHORT).show();
                                    });

                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void openAsAccount(String statusUrl, AccountEntity account) {
        accountManager.setActiveAccount(account);
        Intent intent = new Intent(getContext(), FlowActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(FlowActivity.STATUS_URL, statusUrl);
        startActivity(intent);
        ((BaseActivity) getActivity()).finishWithoutSlideOutAnimation();
    }

    private void showOpenAsDialog(String statusUrl, CharSequence dialogTitle) {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.showAccountChooserDialog(dialogTitle, false, account -> openAsAccount(statusUrl, account));
    }

    private void downloadAllMedia(Status status) {
        Toast.makeText(getContext(), R.string.downloading_media, Toast.LENGTH_SHORT).show();
        for (Attachment attachment : status.getAttachments()) {
            String url = attachment.getUrl();
            Uri uri = Uri.parse(url);
            String filename = uri.getLastPathSegment();

            DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            downloadManager.enqueue(request);
        }
    }

    private void requestDownloadAllMedia(Status status) {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ((BaseActivity) getActivity()).requestPermissions(permissions, (permissions1, grantResults) -> {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadAllMedia(status);
            } else {
                Toast.makeText(getContext(), R.string.error_media_download_permission, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
