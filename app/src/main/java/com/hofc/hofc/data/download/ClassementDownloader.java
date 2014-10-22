package com.hofc.hofc.data.download;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

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
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ClassementLineVO;

import android.os.AsyncTask;
import android.util.Log;

public class ClassementDownloader extends AsyncTask<Void, Void, Integer> {

	private FragmentCallback callback;
	
	public ClassementDownloader(FragmentCallback pCallback) {
		callback = pCallback;
	}
	
	@Override
	protected void onPreExecute() {

        super.onPreExecute();
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		Log.i(ClassementDownloader.class.getName(), "Start downloading Classement informations");
		InputStream inputStream;
		String result;
		
		StringBuilder stringBuilder = new StringBuilder("http://");
		stringBuilder.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuilder.append(":");
			stringBuilder.append(ServerConstant.SERVER_PORT);
		}
		stringBuilder.append("/");
		stringBuilder.append(ServerConstant.CLASSEMENT_CONTEXT);
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(stringBuilder.toString()));
			
			inputStream = httpResponse.getEntity().getContent();
			
			if(inputStream != null) {
				result = HOFCUtils.convertInputStreamToString(inputStream);
				JSONArray jsonArray = new JSONArray(result);
				ArrayList<ClassementLineVO> classementList = new ArrayList<ClassementLineVO>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					ClassementLineVO classement = new ClassementLineVO();
					classement.setNom(object.getString("nom"));
					classement.setPoints(object.getInt("points"));
					classement.setJoue(object.getInt("joue"));
					classement.setGagne(object.getInt("gagne"));
					classement.setNul(object.getInt("nul"));
					classement.setPerdu(object.getInt("perdu"));
					classement.setBc(object.getInt("bc"));
					classement.setBp(object.getInt("bp"));
					classement.setDiff(object.getInt("diff"));
					classementList.add(classement);
				}
				DataSingleton.setClassement(classementList);
				// Sauvegarde en base
				ClassementBDD.insertList(classementList);
				ClassementBDD.updateDateSynchro(new Date());
			} else {
                Log.e(ClassementDownloader.class.getName(), "Problem when contacting server, inputStream is null");
			}
		} catch (ClientProtocolException e) {
            Log.e(ClassementDownloader.class.getName(), "Problem when contacting server", e);
		} catch (IOException e) {
            Log.e(ClassementDownloader.class.getName(), "Problem when contacting server", e);
			return -1;
		} catch (JSONException e) {
            Log.e(ClassementDownloader.class.getName(), "Problem when getting json response", e);
        }

        Log.i(ClassementDownloader.class.getName(), "Finish downloading Classement informations");
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
