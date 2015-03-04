package com.hofc.hofc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.Point;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hofc.hofc.adapter.GridImageAdapter;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.PreCachingLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anthony on 01/03/2015.
 */
public class ActusImageGrid extends ActionBarActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;

    String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        progressBar = (ProgressBar)findViewById(R.id.grid_progress);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.grid_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOFC");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);

        recyclerView.setLayoutManager(new PreCachingLayoutManager(this, 2, p.y));

        this.url = (String) getIntent().getExtras().get("URL");
        RequestQueue requestQueue = ((HOFCApplication) getApplication()).getRequestQueue();
        if(DataSingleton.getCachedImageUrls(url) == null) {
            this.downloadListImage(requestQueue, this.url);
        } else {
            downloadDone(DataSingleton.getCachedImageUrls(url));
        }
    }

    private void downloadDone(List<String> imageUrls) {
        DataSingleton.insertImageCacheUrls(this.url, imageUrls);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new GridImageAdapter(this, imageUrls, this.url));
    }

    private void downloadError() {
        Toast.makeText(this, getString(R.string.connexion_error), Toast.LENGTH_SHORT).show();

    }

    private void downloadListImage(RequestQueue requestQueue, String url) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(ServerConstant.PARSE_PAGE_CONTEXT);
        final Map<String,String> params = new HashMap<>();
        params.put("url", url);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, stringBuilder.toString(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<String> imageUrls = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String imageUrl = jsonArray.getString(i);
                        imageUrls.add(imageUrl);
                    }
                    downloadDone(imageUrls);
                } catch (JSONException e) {
                    downloadError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                downloadError();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                return pars;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }
}
