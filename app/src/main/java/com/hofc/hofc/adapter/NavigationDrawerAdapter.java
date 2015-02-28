package com.hofc.hofc.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hofc.hofc.R;
import com.hofc.hofc.vo.NavigationDrawerItem;

import java.util.List;

/**
 * Created by Fixe on 28/02/2015.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<NavigationDrawerItem> {
    private final Context context;

    public NavigationDrawerAdapter(Context context, int resource, List<NavigationDrawerItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        NavigationDrawerItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.navigation_drawer_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.navigation_drawer_text);
            holder.imageView = (ImageView) convertView.findViewById(R.id.navigation_drawer_image);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.textView.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());

        return convertView;
    }
}
