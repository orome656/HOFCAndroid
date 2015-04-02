package com.hofc.hofc.data.download;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.vo.AgendaLineVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AgendaDownloader {
    /**
     * Update Agenda informations from server
     * @param requestQueue The Volley request queue to add the produced request
     * @param callback Callback to call at the end of the request
     */
    public static void update(RequestQueue requestQueue, final FragmentCallback callback) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(ServerConstant.AGENDA_CONTEXT);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(stringBuilder.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            ArrayList<AgendaLineVO> agendaList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                AgendaLineVO agenda = new AgendaLineVO();
                                agenda.setTitle(object.getString("title"));
                                agenda.setEquipe1(object.getString("equipe1"));
                                agenda.setEquipe2(object.getString("equipe2"));
                                if (!"null".equalsIgnoreCase(object.getString("score1")) && !"null".equalsIgnoreCase(object.getString("score2"))) {
                                    agenda.setScore1(object.getInt("score1"));
                                    agenda.setScore2(object.getInt("score2"));
                                } else {
                                    agenda.setScore1(null);
                                    agenda.setScore2(null);
                                }
                                try {
                                    agenda.setDate(sdf.parse(object.getString("date")));
                                } catch (ParseException e) {
                                    Log.e(AgendaDownloader.class.getName(), "Problem when parsing date", e);
                                    agenda.setDate(null);
                                }
                                agendaList.add(agenda);
                            }
                            DataSingleton.setAgenda(agendaList);
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
