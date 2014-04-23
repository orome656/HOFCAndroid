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
import com.hofc.hofc.vo.ClassementLineVO;

import android.os.AsyncTask;

public class ClassementDownloader extends AsyncTask<Void, Void, Integer> {

	private FragmentCallback callback;
	
	public ClassementDownloader(FragmentCallback pCallback) {
		callback = pCallback;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		
		InputStream inputStream = null;
		String result = "";
		
		StringBuffer stringBuffer = new StringBuffer("http://");
		stringBuffer.append(ServerConstant.SERVER_URL);
		if(ServerConstant.SERVER_PORT != 0) {
			stringBuffer.append(":");
			stringBuffer.append(ServerConstant.SERVER_PORT);
		}
		stringBuffer.append("/");
		stringBuffer.append(ServerConstant.CLASSEMENT_CONTEXT);
		
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			HttpResponse httpResponse = httpClient.execute(new HttpGet(stringBuffer.toString()));
			
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
			} else {
				// TODO Erreur
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		if(result.intValue() == -1) {
			callback.onError();
		} else {
			callback.onTaskDone();
		}
		super.onPostExecute(result);
	}

}
