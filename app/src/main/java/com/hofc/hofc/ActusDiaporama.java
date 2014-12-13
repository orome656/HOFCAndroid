package com.hofc.hofc;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hofc.hofc.adapter.DiaporamaAdapter;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ActusDetailsVO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ActusDiaporama extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actus_diaporama);
        new DiaporamaDownloader().execute((String)getIntent().getExtras().get("URL"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actus_diaporama, menu);
        return true;
    }


    private void initAdapter(List<String> result) {
        ViewPager pager = (ViewPager)findViewById(R.id.view_pager);
        DiaporamaAdapter adapter = new DiaporamaAdapter(this,result);
        pager.setAdapter(adapter);
    }

    private class DiaporamaDownloader extends AsyncTask<String, Void, List<String>> {

        @Override
        protected List<String> doInBackground(String... params) {
            String url = params[0];

            InputStream inputStream;
            String result;
            HttpClient httpClient = new DefaultHttpClient();
            List<String> listeImageUrl = new ArrayList<String>();

            StringBuilder stringBuilder = new StringBuilder("http://");
            stringBuilder.append(ServerConstant.SERVER_URL);
            if(ServerConstant.SERVER_PORT != 0) {
                stringBuilder.append(":");
                stringBuilder.append(ServerConstant.SERVER_PORT);
            }
            stringBuilder.append("/");
            stringBuilder.append(ServerConstant.PARSE_PAGE_CONTEXT);

            try {
                HttpPost httpPost = new HttpPost(stringBuilder.toString());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("url", url));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);

                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null) {
                    result = HOFCUtils.convertInputStreamToString(inputStream);
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i=0;i<jsonArray.length();i++) {
                        listeImageUrl.add(jsonArray.getString(i));
                    }

                } else {
                    Log.e(DiaporamaDownloader.class.getName(), "Problem when contacting server, inputStream is null");
                }
            } catch (ClientProtocolException e) {
                Log.e(DiaporamaDownloader.class.getName(), "Problem when contacting server", e);
            } catch (JSONException e) {
                Log.e(DiaporamaDownloader.class.getName(), "Problem when parsing server response", e);
            } catch (IOException e) {
                Log.e(DiaporamaDownloader.class.getName(), "Problem when contacting server", e);
            }
            return listeImageUrl;
        }


        @Override
        protected void onPostExecute(List<String> result) {
            if(result == null) {
                // TODO gérer les problèmes
            } else {
                initAdapter(result);
            }
            super.onPostExecute(result);
        }
    }

}
