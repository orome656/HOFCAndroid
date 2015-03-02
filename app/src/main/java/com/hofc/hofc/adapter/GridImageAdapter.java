package com.hofc.hofc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hofc.hofc.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by Anthony on 01/03/2015.
 */
public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> {

    private List<String> imageUrls;
    private ImageLoader imageLoader;
    public GridImageAdapter(Context context, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public GridImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler, parent, false);
        ViewHolder vh = new ViewHolder((ImageView)v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        imageLoader.displayImage(imageUrls.get(position), holder.imageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(ImageView v) {
            super(v);
            imageView = v;
        }
    }
}
