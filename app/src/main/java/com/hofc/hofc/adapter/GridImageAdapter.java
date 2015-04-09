package com.hofc.hofc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hofc.hofc.ActusDiaporama;
import com.hofc.hofc.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Anthony on 01/03/2015.
 */
public class GridImageAdapter extends RecyclerView.Adapter<GridImageAdapter.ViewHolder> implements View.OnClickListener {

    private final List<String> imageUrls;
    private final ImageLoader imageLoader;
    private final Context context;
    private final String url;
    public GridImageAdapter(Context context, List<String> imageUrls, String url) {
        this.imageUrls = imageUrls;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        this.url = url;
    }

    @Override
    public GridImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_recycler, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder((ImageView)v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setTag(position);
        imageLoader.displayImage(imageUrls.get(position), holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        Intent i = new Intent(context, ActusDiaporama.class);
        i.putExtra("URL", this.url);
        i.putExtra("position", position);
        context.startActivity(i);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView imageView;
        public ViewHolder(ImageView v) {
            super(v);
            imageView = v;
        }
    }
}
