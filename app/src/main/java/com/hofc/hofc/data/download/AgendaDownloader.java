package com.hofc.hofc.data.download;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.AgendaLineVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AgendaDownloader {
    /**
     * Update Agenda informations from server
     * @param requestQueue The Volley request queue to add the produced request
     * @param callback Callback to call at the end of the request
     */
    public static void update(RequestQueue requestQueue, final FragmentCallback callback, final String dateArgument) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(ServerConstant.AGENDA_CONTEXT);

        if(dateArgument != null && !dateArgument.isEmpty()) {
            stringBuilder.append("/").append(dateArgument);
        }

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
                            if(dateArgument != null && !dateArgument.isEmpty()) {
                                DataSingleton.setAgenda(dateArgument, agendaList);
                            } else {
                                DataSingleton.setAgenda(HOFCUtils.getCurrentWeekMonday(),agendaList);
                            }
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
                if(error == null || error.networkResponse == null ) {
                    callback.onError(R.string.internal_error);
                } else if(error.networkResponse.statusCode == 504) {
                    callback.onError(R.string.timeout_error);
                } else if (error.networkResponse.statusCode == 404) {
                    callback.onError(R.string.internal_error);
                } else if (error.networkResponse.statusCode == 500 || error.networkResponse.statusCode == 503) {
                    callback.onError(R.string.server_error);
                } else {
                    callback.onError(R.string.internal_error);
                }
            }
        });
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonRequest);
    }
}
