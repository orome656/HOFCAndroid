package com.hofc.hofc.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hofc.hofc.R;
import com.hofc.hofc.data.download.ImageDownloader;

import java.util.List;

/**
 * Created by Anthony on 13/12/2014.
 */
public class DiaporamaAdapter extends PagerAdapter {
    Context context;
    List<String> imagesUrl;
    LayoutInflater layoutInflater;
    public DiaporamaAdapter(Context context, List<String> imagesUrl) {
        this.context = context;
        this.imagesUrl = imagesUrl;
    }

    @Override
    public int getCount() {
        return (imagesUrl == null)? 0 : imagesUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        /*layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.image_diaporama, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image_diaporama);
        new ImageDownloader(imageView, null).execute(imagesUrl.get(position));
        */
        ImageView imageView = new ImageView(context);
        new ImageDownloader(imageView, null).execute(imagesUrl.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
