package com.hofc.hofc.data.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hofc.hofc.FragmentCallback;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.os.AsyncTask;

public class CalendrierDownloader extends AsyncTask<Void, Void, Void> {

	private FragmentCallback callback;
	
	public CalendrierDownloader(FragmentCallback pCallback) {
		callback = pCallback;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		
		InputStream inputStream = null;
		String result = "";
		
		StringBuffer stringBuffer = new StringBuffer("http://");
		stringBuffer.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuffer.append(":");
			stringBuffer.append(ServerConstant.SERVER_PORT);
		}
		stringBuffer.append("/");
		stringBuffer.append(ServerConstant.CALENDRIER_CONTEXT);
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(stringBuffer.toString()));
			
			inputStream = httpResponse.getEntity().getContent();
			
			if(inputStream != null) {
				result = HOFCUtils.convertInputStreamToString(inputStream);
				JSONArray jsonArray = new JSONArray(result);
				ArrayList<CalendrierLineVO> calendrierList = new ArrayList<CalendrierLineVO>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					CalendrierLineVO calendrier = new CalendrierLineVO();
					calendrier.setEquipe1(object.getString("equipe1"));
					calendrier.setEquipe2(object.getString("equipe2"));
					calendrier.setScore1(object.getInt("score1"));
					calendrier.setScore2(object.getInt("score2"));
					calendrierList.add(calendrier);
				}
				DataSingleton.setCalendrier(calendrierList);
			} else {
				// TODO Erreur
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		callback.onTaskDone();
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
