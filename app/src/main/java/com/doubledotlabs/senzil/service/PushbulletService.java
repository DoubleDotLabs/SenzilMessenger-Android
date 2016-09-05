package com.doubledotlabs.senzil.service;

import com.doubledotlabs.senzil.data.ConversationLegacy;
import com.doubledotlabs.senzil.mmssms.Message;
import com.doubledotlabs.senzil.mmssms.Transaction;
import com.doubledotlabs.senzil.transaction.NotificationManager;
import com.doubledotlabs.senzil.transaction.SmsHelper;
import com.doubledotlabs.senzil.ui.popup.QKReplyActivity;
import com.pushbullet.android.extension.MessagingExtension;

public class PushbulletService extends MessagingExtension {
    private final String TAG = "PushbulletService";

    @Override
    protected void onMessageReceived(String conversationIden, String body) {
        long threadId = Long.parseLong(conversationIden);
        ConversationLegacy conversation = new ConversationLegacy(getApplicationContext(), threadId);

        Transaction sendTransaction = new Transaction(getApplicationContext(), SmsHelper.getSendSettings(getApplicationContext()));
        Message message = new com.doubledotlabs.senzil.mmssms.Message(body, conversation.getAddress());
        message.setType(com.doubledotlabs.senzil.mmssms.Message.TYPE_SMSMMS);
        sendTransaction.sendNewMessage(message, conversation.getThreadId());

        QKReplyActivity.dismiss(conversation.getThreadId());

        NotificationManager.update(getApplicationContext());
    }

    @Override
    protected void onConversationDismissed(String conversationIden) {
        long threadId = Long.parseLong(conversationIden);
        ConversationLegacy conversation = new ConversationLegacy(getApplicationContext(), threadId);
        conversation.markRead();
    }

}
