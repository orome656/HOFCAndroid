package com.hofc.hofc.data.download;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.utils.HOFCUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by maladota on 18/05/2015.
 */
public class ParamsDownloader {

    public static void update(RequestQueue requestQueue) {
        String url = HOFCUtils.buildUrl(ServerConstant.PARAMS_CONTEXT, null);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    DataSingleton.getParams().setSeasonMatchCount(response.getInt("SEASON_MATCHS_COUNT"));
                } catch (JSONException e) {
                    Log.e(ParamsDownloader.class.getName(), "Deserialization error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //HOFCUtils.handleDownloadError(error, callback);
                // TODO ajouter l'affichage d'un message d'erreur lors d'un probleme de recuperation des parametres
                Log.e(ParamsDownloader.class.getName(), "Error while getting parameter", error);
            }
        });
        requestQueue.add(jsonRequest);
    }
}
