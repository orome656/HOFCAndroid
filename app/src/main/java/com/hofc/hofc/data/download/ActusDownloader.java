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

import android.os.AsyncTask;
import android.util.Log;

import com.hofc.hofc.FragmentCallback;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ActuVO;

public class ActusDownloader extends AsyncTask<Void, Void, Integer> {

	private FragmentCallback callback;
	
	public ActusDownloader(FragmentCallback pCallback) {
		callback = pCallback;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
        Log.i(ActusDownloader.class.getName(), "Start downloading Actus informations");
		
		InputStream inputStream;
		String result;
		
		StringBuilder stringBuilder = new StringBuilder("http://");
		stringBuilder.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuilder.append(":");
			stringBuilder.append(ServerConstant.SERVER_PORT);
		}
		stringBuilder.append("/");
		stringBuilder.append(ServerConstant.ACTUS_CONTEXT);
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(stringBuilder.toString()));
			
			inputStream = httpResponse.getEntity().getContent();
			
			if(inputStream != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
				result = HOFCUtils.convertInputStreamToString(inputStream);
				JSONArray jsonArray = new JSONArray(result);
				ArrayList<ActuVO> actusList = new ArrayList<ActuVO>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					ActuVO actu = new ActuVO();
					actu.setPostId(object.getInt("postId"));
					actu.setTitre(object.getString("titre"));
					actu.setTexte(object.getString("texte"));
					actu.setUrl(object.getString("url"));
					actu.setImageUrl(object.getString("image"));
					try {
						actu.setDate(sdf.parse(object.getString("date")));
					} catch (ParseException e) {
                        Log.e(ActusDownloader.class.getName(), "Problem when parsing date", e);
						actu.setDate(null);
					}
					actusList.add(actu);
				}
				DataSingleton.setActus(actusList);
				// Sauvegarde en base
				ActusBDD.insertList(actusList);
				ActusBDD.updateDateSynchro(new Date());
			} else {
                Log.e(ActusDownloader.class.getName(), "Problem when contacting server, inputStream is null");
            }
		} catch (ClientProtocolException e) {
            Log.e(ActusDownloader.class.getName(), "Problem when contacting server", e);
		} catch (JSONException e) {
            Log.e(ActusDownloader.class.getName(), "Problem when parsing server response", e);
		} catch (IOException e) {
            Log.e(ActusDownloader.class.getName(), "Problem when contacting server", e);
			return -1;
		}

        Log.i(ActusDownloader.class.getName(), "Finish downloading Actus informations");
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
