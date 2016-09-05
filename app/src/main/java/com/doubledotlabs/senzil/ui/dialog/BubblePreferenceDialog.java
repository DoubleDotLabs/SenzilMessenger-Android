package com.doubledotlabs.senzil.ui.dialog;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.doubledotlabs.senzil.R;
import com.doubledotlabs.senzil.ui.ThemeManager;
import com.doubledotlabs.senzil.ui.settings.SettingsFragment;
import com.doubledotlabs.senzil.ui.view.QKSwitchPreference;
import com.doubledotlabs.senzil.ui.view.QKTextView;

public class BubblePreferenceDialog extends QKDialog {
    private static final String TAG = "BubblePreferenceDialog";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        mResources = mContext.getResources();

        View view = mContext.getLayoutInflater().inflate(R.layout.dialog_bubbles, null);

        final QKTextView in1 = (QKTextView) view.findViewById(R.id.in_1);
        in1.setBackgroundResource(ThemeManager.getReceivedBubbleRes());
        in1.getBackground().setColorFilter(ThemeManager.getReceivedBubbleColor(), PorterDuff.Mode.SRC_ATOP);
        in1.setOnColorBackground(ThemeManager.getReceivedBubbleColor() == ThemeManager.getColor());

        final QKTextView in2 = (QKTextView) view.findViewById(R.id.in_2);
        in2.setBackgroundResource(ThemeManager.getReceivedBubbleRes());
        in2.getBackground().setColorFilter(ThemeManager.getReceivedBubbleColor(), PorterDuff.Mode.SRC_ATOP);
        in2.setOnColorBackground(ThemeManager.getReceivedBubbleColor() == ThemeManager.getColor());

        final QKTextView out1 = (QKTextView) view.findViewById(R.id.out_1);
        out1.setBackgroundResource(ThemeManager.getSentBubbleRes());
        out1.getBackground().setColorFilter(ThemeManager.getSentBubbleColor(), PorterDuff.Mode.SRC_ATOP);
        out1.setOnColorBackground(ThemeManager.getSentBubbleColor() == ThemeManager.getColor());

        final QKTextView out2 = (QKTextView) view.findViewById(R.id.out_2);
        out2.setBackgroundResource(ThemeManager.getSentBubbleRes());
        out2.getBackground().setColorFilter(ThemeManager.getSentBubbleColor(), PorterDuff.Mode.SRC_ATOP);
        out2.setOnColorBackground(ThemeManager.getSentBubbleColor() == ThemeManager.getColor());

        Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, preference.getKey());
                switch (preference.getKey()) {
                    case SettingsFragment.COLOR_RECEIVED:
                        ThemeManager.setReceivedBubbleColored(((QKSwitchPreference) preference).isChecked());
                        in1.getBackground().setColorFilter(ThemeManager.getReceivedBubbleColor(), PorterDuff.Mode.SRC_ATOP);
                        in1.setOnColorBackground(ThemeManager.getReceivedBubbleColor() == ThemeManager.getColor());
                        in2.getBackground().setColorFilter(ThemeManager.getReceivedBubbleColor(), PorterDuff.Mode.SRC_ATOP);
                        in2.setOnColorBackground(ThemeManager.getReceivedBubbleColor() == ThemeManager.getColor());
                        return true;

                    case SettingsFragment.COLOR_SENT:
                        ThemeManager.setSentBubbleColored(((QKSwitchPreference) preference).isChecked());
                        out1.getBackground().setColorFilter(ThemeManager.getSentBubbleColor(), PorterDuff.Mode.SRC_ATOP);
                        out1.setOnColorBackground(ThemeManager.getSentBubbleColor() == ThemeManager.getColor());
                        out2.getBackground().setColorFilter(ThemeManager.getSentBubbleColor(), PorterDuff.Mode.SRC_ATOP);
                        out2.setOnColorBackground(ThemeManager.getSentBubbleColor() == ThemeManager.getColor());
                        return true;
                }
                return false;
            }
        };

        LinearLayout prefsLayout = (LinearLayout) view.findViewById(R.id.prefs);
        prefsLayout.addView(new QKSwitchPreference(mContext, onPreferenceClickListener, SettingsFragment.COLOR_RECEIVED,
                prefs, false, R.string.pref_color_received, 0).getView());
        prefsLayout.addView(new QKSwitchPreference(mContext, onPreferenceClickListener, SettingsFragment.COLOR_SENT,
                prefs, true, R.string.pref_color_sent, 0).getView());

        setTitle(R.string.pref_bubbles);
        setCustomView(view);
        setPositiveButton(R.string.okay, null);

        return super.onCreateDialog(savedInstanceState);
    }
}
