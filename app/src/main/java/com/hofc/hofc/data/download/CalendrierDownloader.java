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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.fragment.CalendrierFragment;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.CalendrierLineVO;

import android.os.AsyncTask;
import android.util.Log;

public class CalendrierDownloader {
    /**
     *
     * @param requestQueue
     * @param callback
     */
    public static void update(RequestQueue requestQueue, final FragmentCallback callback) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(ServerConstant.CALENDRIER_CONTEXT);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(stringBuilder.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                            ArrayList<CalendrierLineVO> calendrierList = new ArrayList<CalendrierLineVO>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                CalendrierLineVO calendrier = new CalendrierLineVO();
                                calendrier.setEquipe1(object.getString("equipe1"));
                                calendrier.setEquipe2(object.getString("equipe2"));
                                if (!"null".equalsIgnoreCase(object.getString("score1")) && !"null".equalsIgnoreCase(object.getString("score2"))) {
                                    calendrier.setScore1(object.getInt("score1"));
                                    calendrier.setScore2(object.getInt("score2"));
                                } else {
                                    calendrier.setScore1(null);
                                    calendrier.setScore2(null);
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
                            return 0;
                        } catch (JSONException e) {
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
