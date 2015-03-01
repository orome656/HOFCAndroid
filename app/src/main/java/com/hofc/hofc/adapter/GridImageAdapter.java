package com.hofc.hofc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Anthony on 01/03/2015.
 */
public class GridImageAdapter extends BaseAdapter{

    private Context context;
    private List<String> imageUrls;
    private ImageLoader imageLoader;
    public GridImageAdapter(Context context, List<String> imageUrls) {
        this.imageUrls = imageUrls;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null ) {
            convertView = new ImageView(context);
        }

        imageLoader.displayImage(imageUrls.get(position),((ImageView) convertView));
        return convertView;
    }
}
