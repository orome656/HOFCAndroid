package com.hofc.hofc.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by maladota on 02/03/2015.
 */
public class PreCachingLayoutManager extends GridLayoutManager {
    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 600;
    private int extraLayoutSpace = -1;
    private final Context context;

    public PreCachingLayoutManager(Context context, int count) {
        super(context, count);
        this.context = context;
    }

    public PreCachingLayoutManager(Context context, int count, int extraLayoutSpace) {
        super(context, count);
        this.context = context;
        this.extraLayoutSpace = extraLayoutSpace;
    }

    public PreCachingLayoutManager(Context context, int count, int orientation, boolean reverseLayout) {
        super(context,count, orientation, reverseLayout);
        this.context = context;
    }

    public void setExtraLayoutSpace(int extraLayoutSpace) {
        this.extraLayoutSpace = extraLayoutSpace;
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (extraLayoutSpace > 0) {
            return extraLayoutSpace;
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}
