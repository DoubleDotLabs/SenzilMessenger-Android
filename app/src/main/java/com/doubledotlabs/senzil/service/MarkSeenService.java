package com.doubledotlabs.senzil.service;

import android.app.IntentService;
import android.content.Intent;

import com.doubledotlabs.senzil.transaction.NotificationManager;
import com.doubledotlabs.senzil.transaction.SmsHelper;

public class MarkSeenService extends IntentService {
    private final String TAG = "MarkSeenService";

    public MarkSeenService() {
        super("MarkSeenService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SmsHelper.markSmsSeen(this);
        SmsHelper.markMmsSeen(this);
        NotificationManager.update(this);
    }
}
