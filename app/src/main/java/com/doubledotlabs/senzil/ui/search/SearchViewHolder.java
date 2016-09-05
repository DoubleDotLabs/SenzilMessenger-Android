package com.doubledotlabs.senzil.ui.search;

import android.view.View;

import com.doubledotlabs.senzil.R;
import com.doubledotlabs.senzil.ui.base.ClickyViewHolder;
import com.doubledotlabs.senzil.ui.base.QKActivity;
import com.doubledotlabs.senzil.ui.view.AvatarView;
import com.doubledotlabs.senzil.ui.view.QKTextView;

public class SearchViewHolder extends ClickyViewHolder<SearchData> {

    protected View root;
    protected AvatarView avatar;
    protected QKTextView name;
    protected QKTextView date;
    protected QKTextView snippet;

    public SearchViewHolder(QKActivity context, View view) {
        super(context, view);

        root = view;
        avatar = (AvatarView) view.findViewById(R.id.search_avatar);
        name = (QKTextView) view.findViewById(R.id.search_name);
        date = (QKTextView) view.findViewById(R.id.search_date);
        snippet = (QKTextView) view.findViewById(R.id.search_snippet);
    }
}
