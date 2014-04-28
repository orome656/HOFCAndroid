package com.hofc.hofc.adapter;

import com.hofc.hofc.data.DataSingleton;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ClassementAdapter extends BaseAdapter {

	LayoutInflater inflater;
	
	public ClassementAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return DataSingleton.getClassement().size();
	}

	@Override
	public Object getItem(int position) {
		return DataSingleton.getClassement().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private class ViewHolder {
		TextView classementNom;
	}

}
