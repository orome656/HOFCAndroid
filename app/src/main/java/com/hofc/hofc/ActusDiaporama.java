package com.hofc.hofc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hofc.hofc.adapter.DiaporamaAdapter;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.utils.HOFCUtils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ActusDiaporama extends ActionBarActivity {

    private ProgressBar progressBar;
    private String url;
    private int initialPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actus_diaporama);
        progressBar = (ProgressBar)findViewById(R.id.diaporama_progress);

        Toolbar toolbar = (Toolbar)findViewById(R.id.diaporama_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.url = (String)getIntent().getExtras().get("URL");
        this.initialPosition = (int)getIntent().getExtras().get("position");
        initAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actus_diaporama, menu);
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
    private void initAdapter() {
        ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
        DiaporamaAdapter adapter = new DiaporamaAdapter(this, DataSingleton.getCachedImageUrls(this.url));
        pager.setAdapter(adapter);
        pager.setCurrentItem(initialPosition);
        progressBar.setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);
    }
}
