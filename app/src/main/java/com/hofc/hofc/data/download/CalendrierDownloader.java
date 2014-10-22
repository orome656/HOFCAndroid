package com.hofc.hofc.data.download;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.os.AsyncTask;
import android.util.Log;

public class CalendrierDownloader extends AsyncTask<Void, Void, Integer> {

	private FragmentCallback callback;
	
	public CalendrierDownloader(FragmentCallback pCallback) {
		callback = pCallback;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		Log.i(CalendrierDownloader.class.getName(), "Start downloading Calendrier informations");
		InputStream inputStream;
		String result;
		
		StringBuilder stringBuilder = new StringBuilder("http://");
		stringBuilder.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuilder.append(":");
			stringBuilder.append(ServerConstant.SERVER_PORT);
		}
		stringBuilder.append("/");
		stringBuilder.append(ServerConstant.CALENDRIER_CONTEXT);
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(stringBuilder.toString()));
			
			inputStream = httpResponse.getEntity().getContent();
			
			if(inputStream != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss", Locale.getDefault());
				result = HOFCUtils.convertInputStreamToString(inputStream);
				JSONArray jsonArray = new JSONArray(result);
				ArrayList<CalendrierLineVO> calendrierList = new ArrayList<CalendrierLineVO>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					CalendrierLineVO calendrier = new CalendrierLineVO();
					calendrier.setEquipe1(object.getString("equipe1"));
					calendrier.setEquipe2(object.getString("equipe2"));
                    if(!"null".equalsIgnoreCase(object.getString("score1")) && !"null".equalsIgnoreCase(object.getString("score2"))) {
                        calendrier.setScore1(object.getInt("score1"));
                        calendrier.setScore2(object.getInt("score2"));
                    }
					try {
						calendrier.setDate(sdf.parse(object.getString("date")));
					} catch (ParseException e) {
                        Log.e(CalendrierDownloader.class.getName(), "Problem when parsing date", e);
                        calendrier.setDate(null);
					}
					calendrierList.add(calendrier);
				}
				DataSingleton.setCalendrier(calendrierList);
				// Sauvegarde en base
				CalendrierBDD.insertList(calendrierList);
				CalendrierBDD.updateDateSynchro(new Date());
			} else {
                Log.e(CalendrierDownloader.class.getName(), "Problem while contacting server");
			}
		} catch (ClientProtocolException e) {
            Log.e(CalendrierDownloader.class.getName(), "Problem while contacting server", e);
		} catch (JSONException e) {
            Log.e(CalendrierDownloader.class.getName(), "Problem while parsing server response", e);
		} catch (IOException e) {
            Log.e(CalendrierDownloader.class.getName(), "Problem while contacting server", e);
			return -1;
		}

        Log.i(CalendrierDownloader.class.getName(), "Finish downloading Calendrier informations");
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if(result == -1) {
			callback.onError();
		} else {
			callback.onTaskDone();
		}
		super.onPostExecute(result);
	}

}
