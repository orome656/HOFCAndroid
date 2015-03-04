package com.hofc.hofc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hofc.hofc.R;
import com.hofc.hofc.utils.TouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Adapter for Diaporama
 * Created by Anthony on 13/12/2014.
 */
public class DiaporamaAdapter extends PagerAdapter {
    Context context;
    List<String> imagesUrl;
    LayoutInflater layoutInflater;
    ImageLoader imageLoader;

    public DiaporamaAdapter(Context context, List<String> imagesUrl) {
        this.context = context;
        this.imagesUrl = imagesUrl;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return (imagesUrl == null)? 0 : imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(layoutInflater == null)
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = layoutInflater.inflate(R.layout.image_diaporama, null);
        final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.image_diaporama);
        final ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.image_diaporama_progress);
        container.addView(itemView);
        imageLoader.displayImage(imagesUrl.get(position), imageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                super.onLoadingStarted(imageUri, view);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                super.onLoadingFailed(imageUri, view, failReason);
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
