package com.hofc.hofc.data.download;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.LocalDataSingleton;
import com.hofc.hofc.utils.HOFCUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe de téléchargement des paramètees de l'application
 * Created by maladota on 18/05/2015.
 */
public class ParamsDownloader {

    public static void update(RequestQueue requestQueue) {
        String url = HOFCUtils.buildUrl(ServerConstant.PARAMS_CONTEXT, null);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject response) {
                try {
                    LocalDataSingleton.getParams().setSeasonMatchCount(response.getInt("SEASON_MATCHS_COUNT"));
                    LocalDataSingleton.addParam("seasonMatchCount", response.getInt("SEASON_MATCHS_COUNT") + "");
                    LocalDataSingleton.getParams().setSeasonMatchCountEquipe2(response.getInt("SEASON_MATCHS_COUNT_EQUIPE2"));
                    LocalDataSingleton.addParam("seasonMatchCountEquipe2", response.getInt("SEASON_MATCHS_COUNT_EQUIPE2") + "");
                    LocalDataSingleton.getParams().setSeasonMatchCountEquipe3(response.getInt("SEASON_MATCHS_COUNT_EQUIPE3"));
                    LocalDataSingleton.addParam("seasonMatchCountEquipe3", response.getInt("SEASON_MATCHS_COUNT_EQUIPE3") + "");
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
