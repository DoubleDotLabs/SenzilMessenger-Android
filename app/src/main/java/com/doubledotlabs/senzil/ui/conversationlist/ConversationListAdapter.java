package com.doubledotlabs.senzil.ui.conversationlist;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doubledotlabs.senzil.R;
import com.doubledotlabs.senzil.common.ConversationPrefsHelper;
import com.doubledotlabs.senzil.common.FontManager;
import com.doubledotlabs.senzil.common.LiveViewManager;
import com.doubledotlabs.senzil.common.QKPreferences;
import com.doubledotlabs.senzil.common.emoji.EmojiRegistry;
import com.doubledotlabs.senzil.common.utils.DateFormatter;
import com.doubledotlabs.senzil.data.Contact;
import com.doubledotlabs.senzil.data.Conversation;
import com.doubledotlabs.senzil.enums.QKPreference;
import com.doubledotlabs.senzil.ui.ThemeManager;
import com.doubledotlabs.senzil.ui.base.QKActivity;
import com.doubledotlabs.senzil.ui.base.RecyclerCursorAdapter;
import com.doubledotlabs.senzil.ui.settings.SettingsFragment;

public class ConversationListAdapter extends RecyclerCursorAdapter<ConversationListViewHolder, Conversation> {


    private final SharedPreferences mPrefs;

    public ConversationListAdapter(QKActivity context) {
        super(context);
        mPrefs = mContext.getPrefs();
    }

    protected Conversation getItem(int position) {
        mCursor.moveToPosition(position);
        return Conversation.from(mContext, mCursor);
    }

    @Override
    public ConversationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_conversation, null);

        ConversationListViewHolder holder = new ConversationListViewHolder(mContext, view);
        holder.mutedView.setImageResource(R.drawable.ic_notifications_muted);
        holder.unreadView.setImageResource(R.drawable.ic_unread_indicator);
        holder.errorIndicator.setImageResource(R.drawable.ic_error);

        LiveViewManager.registerView(QKPreference.THEME, this, key -> {
            holder.mutedView.setColorFilter(ThemeManager.getColor());
            holder.unreadView.setColorFilter(ThemeManager.getColor());
            holder.errorIndicator.setColorFilter(ThemeManager.getColor());
        });

        LiveViewManager.registerView(QKPreference.BACKGROUND, this, key -> {
            holder.root.setBackgroundDrawable(ThemeManager.getRippleBackground());
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ConversationListViewHolder holder, int position) {
        final Conversation conversation = getItem(position);

        holder.mData = conversation;
        holder.mContext = mContext;
        holder.mClickListener = mItemClickListener;
        holder.root.setOnClickListener(holder);
        holder.root.setOnLongClickListener(holder);

        holder.mutedView.setVisibility(new ConversationPrefsHelper(mContext, conversation.getThreadId())
                .getNotificationsEnabled() ? View.GONE : View.VISIBLE);

        holder.errorIndicator.setVisibility(conversation.hasError() ? View.VISIBLE : View.GONE);

        final boolean hasUnreadMessages = conversation.hasUnreadMessages();
        if (hasUnreadMessages) {
            holder.unreadView.setVisibility(View.VISIBLE);
            holder.snippetView.setTextColor(ThemeManager.getTextOnBackgroundPrimary());
            holder.dateView.setTextColor(ThemeManager.getColor());
            holder.fromView.setType(FontManager.TEXT_TYPE_PRIMARY_BOLD);
            holder.snippetView.setMaxLines(5);
        } else {
            holder.unreadView.setVisibility(View.GONE);
            holder.snippetView.setTextColor(ThemeManager.getTextOnBackgroundSecondary());
            holder.dateView.setTextColor(ThemeManager.getTextOnBackgroundSecondary());
            holder.fromView.setType(FontManager.TEXT_TYPE_PRIMARY);
            holder.snippetView.setMaxLines(1);
        }

        LiveViewManager.registerView(QKPreference.THEME, this, key -> {
            holder.dateView.setTextColor(hasUnreadMessages ? ThemeManager.getColor() : ThemeManager.getTextOnBackgroundSecondary());
        });

        if (isInMultiSelectMode()) {
            holder.mSelected.setVisibility(View.VISIBLE);
            if (isSelected(conversation.getThreadId())) {
                holder.mSelected.setImageResource(R.drawable.ic_selected);
                holder.mSelected.setColorFilter(ThemeManager.getColor());
                holder.mSelected.setAlpha(1f);
            } else {
                holder.mSelected.setImageResource(R.drawable.ic_unselected);
                holder.mSelected.setColorFilter(ThemeManager.getTextOnBackgroundSecondary());
                holder.mSelected.setAlpha(0.5f);
            }
        } else {
            holder.mSelected.setVisibility(View.GONE);
        }

        LiveViewManager.registerView(QKPreference.HIDE_AVATAR_CONVERSATIONS, this, key -> {
            holder.mAvatarView.setVisibility(QKPreferences.getBoolean(QKPreference.HIDE_AVATAR_CONVERSATIONS) ? View.GONE : View.VISIBLE);
        });

        // Date
        holder.dateView.setText(DateFormatter.getConversationTimestamp(mContext, conversation.getDate()));

        // Subject
        String emojiSnippet = conversation.getSnippet();
        if (mPrefs.getBoolean(SettingsFragment.AUTO_EMOJI, false)) {
            emojiSnippet = EmojiRegistry.parseEmojis(emojiSnippet);
        }
        holder.snippetView.setText(emojiSnippet);

        Contact.addListener(holder);

        // Update the avatar and name
        holder.onUpdate(conversation.getRecipients().size() == 1 ? conversation.getRecipients().get(0) : null);
    }
}
