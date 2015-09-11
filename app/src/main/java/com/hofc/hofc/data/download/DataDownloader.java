package com.hofc.hofc.data.download;

import android.os.AsyncTask;
import android.util.Log;

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
import com.hofc.hofc.data.HashMapDataSingleton;
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
    public static <T,V extends CommonBDD> void download(RequestQueue requestQueue, String context, String[] params, final FragmentCallback callback, final Class<T> valueClass, final Class<V> databaseClass) {
        String url = HOFCUtils.buildUrl(context, params);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.setDateFormat(sdf);
                            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                            String json = response.toString();
                            ArrayList<T> actusList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, valueClass));

                            DataSingleton.getInstance(valueClass,databaseClass).handleDownload(actusList);
                            return 0;
                        } catch (JsonMappingException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
                            return -1;
                        } catch (JsonParseException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
                            return -1;
                        } catch (IOException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
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

    public static <T,V extends CommonBDD> void downloadWithKey(RequestQueue requestQueue, String context, String[] params, final FragmentCallback callback, final Class<T> valueClass, final Class<V> databaseClass, final String key) {
        String url = HOFCUtils.buildUrl(context, params);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected Integer doInBackground(Void... params) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                            ObjectMapper mapper = new ObjectMapper();
                            mapper.setDateFormat(sdf);
                            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                            String json = response.toString();
                            ArrayList<T> actusList = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, valueClass));

                            HashMapDataSingleton.getInstance(valueClass, databaseClass).handleDownload(key, actusList);
                            return 0;
                        } catch (JsonMappingException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
                            return -1;
                        } catch (JsonParseException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
                            return -1;
                        } catch (IOException e) {
                            Log.e(DataDownloader.class.getName(), "Error while deserialize", e);
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
