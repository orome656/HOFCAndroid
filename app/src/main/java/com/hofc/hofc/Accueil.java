package com.hofc.hofc;

import android.support.v7.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.fragment.ActusFragment;
import com.hofc.hofc.fragment.CalendrierFragment;
import com.hofc.hofc.fragment.ClassementFragment;
import com.hofc.hofc.notification.GcmPreference;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Accueil extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    FragmentManager fragmentManager = null;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ActusFragment actusFragment = null;
    private CalendrierFragment calendrierFragment = null;
    private ClassementFragment classementFragment = null;

    private Context context;
    private GoogleCloudMessaging gcm;
    private String regId;
    private String SENDER_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accueil);
        DataSingleton.initialize(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = getText(R.string.title_accueil);
        getSupportActionBar().setTitle(mTitle);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /**
         * Gestion des notifications, enregistrement auprès du serveur
         */
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(context);
        regId = GcmPreference.getRegistrationId(context);
        if(regId == null || regId.isEmpty()) {
            registerInBackground();
        }

        /**
         * Cache d'image
         */
        if(!ImageLoader.getInstance().isInited()) {
            File cacheDir = StorageUtils.getCacheDirectory(context);

            ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(this)
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .build();

            ImageLoader.getInstance().init(loaderConfiguration);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
    	if(this.fragmentManager == null)
    		fragmentManager = getFragmentManager();
        if(position == 0) {
        	if(this.actusFragment == null) {
        		this.actusFragment = ActusFragment.newInstance();
        	}
        	// Actus
        	fragmentManager.beginTransaction()
            .replace(R.id.container, this.actusFragment)
            .commit();
        	mTitle = getText(R.string.title_accueil);
        } else if(position == 1) {
        	if(this.classementFragment == null) {
        		this.classementFragment = ClassementFragment.newInstance();
        	}
        	// Classement
        	fragmentManager.beginTransaction()
            .replace(R.id.container, this.classementFragment)
            .commit();
        	mTitle = getText(R.string.title_classement);
        } else if (position == 2) {
        	if(this.calendrierFragment == null) {
        		this.calendrierFragment = CalendrierFragment.newInstance();
        	}
        	// Calendrier
        	fragmentManager.beginTransaction()
            .replace(R.id.container, this.calendrierFragment)
            .commit();
        	mTitle = getText(R.string.title_calendrier);
        } else {
        	Log.e("Accueil", "NavigationDrawer number click unknown ");
        }
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitle);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.accueil, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg;
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
                    GcmPreference.setRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                StringBuilder stringBuilder = new StringBuilder("http://");
                stringBuilder.append(ServerConstant.SERVER_URL);
                if(ServerConstant.SERVER_PORT != 0) {
                    stringBuilder.append(":");
                    stringBuilder.append(ServerConstant.SERVER_PORT);
                }
                stringBuilder.append("/");
                stringBuilder.append(ServerConstant.NOTIF_CONTEXT);

                try {
                    HttpPost httpPost = new HttpPost(stringBuilder.toString());
                    List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                    nameValuePairs.add(new BasicNameValuePair("notification_id", regId));
                    nameValuePairs.add(new BasicNameValuePair("uuid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                } catch (IOException ex) {
                    Log.e(Accueil.class.getName(), "Problem while sending Notification ID", ex);
                }
                return null;
            }
        }.execute(null, null, null);
    }
}
