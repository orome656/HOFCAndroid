package com.hofc.hofc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.vo.ActusDetailsVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class ActusDetail extends ActionBarActivity {

    private TextView titleTextView = null;
    private TextView dateTextView = null;
    private WebView contentTextView = null;
    private ProgressBar progressBar = null;

    SimpleDateFormat sdf = null;

    private String HTML_PREFIX = "<html><body style=\"text-align:justify\">";
    private String HTML_SUFIX = "</body></Html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_actus_detail);

        titleTextView = (TextView)findViewById(R.id.actus_details_title);
        dateTextView = (TextView)findViewById(R.id.actus_details_date);
        contentTextView = (WebView)findViewById(R.id.actus_details_content);
        progressBar = (ProgressBar)findViewById(R.id.actus_details_progress);

        sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("HOFC");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        String url = (String)i.getExtras().get("URL");

        RequestQueue requestQueue = ((HOFCApplication) getApplication()).getRequestQueue();
        downloadDetails(requestQueue, url);
        //ActusDetailDownloader downloader = new ActusDetailDownloader();
        //downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actus_detail, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshView(ActusDetailsVO actusDetails) {
        titleTextView.setText(actusDetails.getTitle());
        dateTextView.setText(sdf.format(actusDetails.getDate()));
        contentTextView.loadDataWithBaseURL(null, HTML_PREFIX + actusDetails.getContent() + HTML_SUFIX, "text/html", "utf-8", null);
        contentTextView.setBackgroundColor(Color.TRANSPARENT);
        progressBar.setVisibility(View.GONE);
        titleTextView.setVisibility(View.VISIBLE);
        dateTextView.setVisibility(View.VISIBLE);
        contentTextView.setVisibility(View.VISIBLE);
    }

    private void downloadError() {
        Toast.makeText(this, R.string.connexion_error, Toast.LENGTH_SHORT).show();
    }

    private void downloadDetails(RequestQueue requestQueue, String url) {
        StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
        stringBuilder.append(ServerConstant.SERVER_URL);
        if(ServerConstant.SERVER_PORT != 0) {
            stringBuilder.append(":");
            stringBuilder.append(ServerConstant.SERVER_PORT);
        }
        stringBuilder.append("/");
        stringBuilder.append(ServerConstant.PARSE_PAGE_CONTEXT);
        try {
            JSONObject params = new JSONObject();
            params.put("url", url);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, stringBuilder.toString(), params,new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    ActusDetailsVO actuDetails = new ActusDetailsVO();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    try {
                        actuDetails.setTitle(response.getString("title"));
                        try {
                            actuDetails.setDate(sdf.parse(response.getString("date")));
                        } catch (ParseException e) {
                            actuDetails.setDate(null);
                        }
                        actuDetails.setContent(response.getString("article"));
                        refreshView(actuDetails);
                    } catch(JSONException e) {
                        downloadError();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    downloadError();
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (JSONException e) {
            downloadError();
        }

    }
}
