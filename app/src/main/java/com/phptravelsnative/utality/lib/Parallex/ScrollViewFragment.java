package com.phptravelsnative.utality.lib.Parallex;

import android.widget.ScrollView;

public class ScrollViewFragment extends ScrollTabHolderFragment {

    public static final String TAG = ScrollViewFragment.class.getSimpleName();

    protected static final int NO_SCROLL_X = 0;

    protected NotifyingScrollView mScrollView;

    protected void setScrollViewOnScrollListener() {
        mScrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(ScrollView view, int l, int t, int oldl, int oldt) {

                if (mScrollTabHolder != null) {
                    mScrollTabHolder.onScrollViewScroll(view, l, t, oldl, oldt, mPosition);
                }
            }
        });
    }

    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {
        if (mScrollView == null) return;

        mScrollView.scrollTo(NO_SCROLL_X, headerHeight - scrollHeight);

        if (mScrollTabHolder != null) {
            mScrollTabHolder.onScrollViewScroll(mScrollView, 0, 0, 0, 0, mPosition);
        }
    }
}
