package com.hofc.hofc;

import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ActusDetailsVO;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

        ActusDetailDownloader downloader = new ActusDetailDownloader();
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
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
        progressBar.setVisibility(View.GONE);
        titleTextView.setVisibility(View.VISIBLE);
        dateTextView.setVisibility(View.VISIBLE);
        contentTextView.setVisibility(View.VISIBLE);
    }

    private void downloadError() {
        Toast.makeText(this, R.string.connexion_error, Toast.LENGTH_SHORT).show();
    }

    private class ActusDetailDownloader extends AsyncTask<String, Void, ActusDetailsVO> {
        @Override
        protected ActusDetailsVO doInBackground(String... params) {
            String url = params[0];

            InputStream inputStream;
            String result;
            ActusDetailsVO actuDetails = null;
            HttpClient httpClient = new DefaultHttpClient();

            StringBuilder stringBuilder = new StringBuilder(ServerConstant.SERVER_URL_PREFIX);
            stringBuilder.append(ServerConstant.SERVER_URL);
            if(ServerConstant.SERVER_PORT != 0) {
                stringBuilder.append(":");
                stringBuilder.append(ServerConstant.SERVER_PORT);
            }
            stringBuilder.append("/");
            stringBuilder.append(ServerConstant.PARSE_PAGE_CONTEXT);

            try {
                HttpPost httpPost = new HttpPost(stringBuilder.toString());
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("url", url));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    result = HOFCUtils.convertInputStreamToString(inputStream);
                    JSONObject jsonObject = new JSONObject(result);
                    actuDetails = new ActusDetailsVO();
                    actuDetails.setTitle(jsonObject.getString("title"));
                    try {
                        actuDetails.setDate(sdf.parse(jsonObject.getString("date")));
                    } catch (ParseException e) {
                        actuDetails.setDate(null);
                    }
                    actuDetails.setContent(jsonObject.getString("article"));

                } else {
                    Log.e(ActusDetailDownloader.class.getName(), "Problem when contacting server, inputStream is null");
                }
            } catch (JSONException e) {
                Log.e(ActusDetailDownloader.class.getName(), "Problem when parsing server response", e);
            } catch (IOException e) {
                Log.e(ActusDetailDownloader.class.getName(), "Problem when contacting server", e);
            }
            return actuDetails;
        }

        @Override
        protected void onPostExecute(ActusDetailsVO result) {
            if(result == null) {
                downloadError();
            } else {
                refreshView(result);
            }
            super.onPostExecute(result);
        }
    }

}
