package com.hofc.hofc.data.download;

import com.hofc.hofc.constant.ServerConstant;

import android.os.AsyncTask;

public class ClassementDownloader extends AsyncTask<Void, Void, Void> {

	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		StringBuffer stringBuffer = new StringBuffer("http://");
		stringBuffer.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuffer.append(":");
			stringBuffer.append(ServerConstant.SERVER_PORT);
		}
		stringBuffer.append("/");
		stringBuffer.append(ServerConstant.CLASSEMENT_CONTEXT);
		
		String classementUrl = stringBuffer.toString();
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
