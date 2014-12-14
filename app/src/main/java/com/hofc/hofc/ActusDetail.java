package com.hofc.hofc;

import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.util.SystemUiHider;
import com.hofc.hofc.utils.HOFCUtils;
import com.hofc.hofc.vo.ActusDetailsVO;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ActusDetail extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private TextView titleTextView = null;
    private TextView dateTextView = null;
    private WebView contentTextView = null;

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

        sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");

        getActionBar().setTitle("HOFC");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            /* if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            } */
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            /*
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                                    */
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            // controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        Intent i = getIntent();
        String url = (String)i.getExtras().get("URL");

        ActusDetailDownloader downloader = new ActusDetailDownloader();
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
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
            } catch (ClientProtocolException e) {
                Log.e(ActusDetailDownloader.class.getName(), "Problem when contacting server", e);
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
                // TODO gérer les problèmes
            } else {
                refreshView(result);
            }
            super.onPostExecute(result);
        }
    }
}
