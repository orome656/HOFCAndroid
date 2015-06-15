package com.hofc.hofc.data.download;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ClassementLineVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ClassementDownloader {
    /**
     * Update Classement informations from server
     * @param requestQueue The Volley request queue to add the produced request
     * @param callback Callback to call at the end of the request
     */
    public static void update(RequestQueue requestQueue, final FragmentCallback callback) {
        String url = HOFCUtils.buildUrl(ServerConstant.CLASSEMENT_CONTEXT, null);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            ArrayList<ClassementLineVO> classementList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
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
                            return 0;
                        } catch (JSONException e) {
                            Log.e(ClassementDownloader.class.getName(), "Error while deserialize",e);
                            return -1;
                        }
                    }

                    @Override
                    protected void onPostExecute(Integer result) {
                        if(result == 0) {
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
                HOFCUtils.handleDownloadError(error, callback);
            }
        });

        requestQueue.add(jsonRequest);
    }
}
