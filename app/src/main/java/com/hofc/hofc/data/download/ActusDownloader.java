package com.hofc.hofc.data.download;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ActuVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActusDownloader {

	public static void updateActus(RequestQueue requestQueue, final FragmentCallback callback) {
        String url = HOFCUtils.buildUrl(ServerConstant.ACTUS_CONTEXT, null);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                            ArrayList<ActuVO> actusList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                ActuVO actu = new ActuVO();
                                actu.setPostId(object.getInt("postid"));
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
                            return 0;
                        } catch (JSONException e) {
                            Log.e(ActusDownloader.class.getName(), "Error while deserialize",e);
                            return -1;
                        }
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if (result == 0) {
                            callback.onTaskDone();
                        } else {
                            callback.onError(R.string.internal_error);
                        }
                    }
                }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(R.string.internal_error);
            }
        });

        requestQueue.add(jsonRequest);
    }

}
