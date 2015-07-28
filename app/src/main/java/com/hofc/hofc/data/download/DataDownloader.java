package com.hofc.hofc.data.download;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hofc.hofc.R;
import com.hofc.hofc.data.CommonBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.utils.HOFCUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generic class to download data
 */
public class DataDownloader {
    public static <T,V extends CommonBDD> void download(RequestQueue requestQueue, String context, String[] params, final FragmentCallback callback, final Class<T> classToBind, final Class<V> databaseClass) {
        String url = HOFCUtils.buildUrl(context, params);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                            String json = response.toString();
                            ArrayList<T> actusList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, classToBind));

                            DataSingleton.getInstance(classToBind,databaseClass).set(actusList);
                            return 0;
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                            return -1;
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                            return -1;
                        } catch (IOException e) {
                            e.printStackTrace();
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
                HOFCUtils.handleDownloadError(error, callback);
            }
        });

        requestQueue.add(jsonRequest);
    }
}
