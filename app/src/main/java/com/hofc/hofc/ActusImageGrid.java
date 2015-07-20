package com.hofc.hofc;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.graphics.Point;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hofc.hofc.adapter.GridImageAdapter;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.utils.PreCachingLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anthony on 01/03/2015
 * Affichage de la liste des images en grille.
 */
public class ActusImageGrid extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String url;
    private MaterialDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        dialog = new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .content(R.string.loading_popup)
                .progress(true, 0)
                .build();
        dialog.show();

        Toolbar toolbar = (Toolbar)findViewById(R.id.grid_toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("HOFC");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
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
        dialog.dismiss();
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setAdapter(new GridImageAdapter(this, imageUrls, this.url));
    }

    private void downloadError() {
        Toast.makeText(this, getString(R.string.connexion_error), Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }

    private void downloadListImage(RequestQueue requestQueue, String url) {
        String urlServer = HOFCUtils.buildUrl(ServerConstant.PARSE_PAGE_CONTEXT, null);
        final Map<String,String> params = new HashMap<>();
        params.put("url", url);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, urlServer, new Response.Listener<String>() {

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
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonArrayRequest);
    }
}
