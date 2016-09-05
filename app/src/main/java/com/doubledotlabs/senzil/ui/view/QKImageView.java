package com.doubledotlabs.senzil.ui.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.doubledotlabs.senzil.common.LiveViewManager;
import com.doubledotlabs.senzil.enums.QKPreference;
import com.doubledotlabs.senzil.ui.ThemeManager;

public class QKImageView extends ImageView {

    private static final String TAG = "QKImageView";
    private Drawable mDrawable;

    public QKImageView(Context context) {
        super(context);
        init();
    }

    public QKImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QKImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        LiveViewManager.registerView(QKPreference.THEME, this, key -> {
            setImageDrawable(mDrawable);
        });
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        // Have to set this as null to refresh
        super.setImageDrawable(null);
        mDrawable = drawable;

        if (mDrawable != null) {
            mDrawable.setColorFilter(ThemeManager.getColor(), PorterDuff.Mode.SRC_ATOP);
            super.setImageDrawable(drawable);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
