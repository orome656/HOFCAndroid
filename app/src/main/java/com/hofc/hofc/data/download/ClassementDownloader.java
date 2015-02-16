package com.hofc.hofc.data.download;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.vo.ClassementLineVO;

import android.os.AsyncTask;
import android.util.Log;

public class ClassementDownloader {
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
        stringBuilder.append(ServerConstant.CLASSEMENT_CONTEXT);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(stringBuilder.toString(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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
                    callback.onTaskDone();
                } catch (JSONException e) {
                    callback.onError(R.string.internal_error);
                }
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
