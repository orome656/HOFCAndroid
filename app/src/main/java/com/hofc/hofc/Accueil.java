package com.hofc.hofc;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hofc.hofc.constant.AppConstant;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.ParamsDownloader;
import com.hofc.hofc.fragment.ActusFragment;
import com.hofc.hofc.fragment.AgendaFragment;
import com.hofc.hofc.fragment.CalendrierFragment;
import com.hofc.hofc.fragment.ClassementFragment;
import com.hofc.hofc.fragment.JourneeFragment;
import com.hofc.hofc.notification.GcmPreference;
import com.hofc.hofc.utils.HOFCUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Accueil extends AppCompatActivity {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private DrawerLayout mDrawerLayout;
    private FragmentManager fragmentManager = null;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ActusFragment actusFragment = null;
    private CalendrierFragment calendrierFragment = null;
    private ClassementFragment classementFragment = null;
    private AgendaFragment agendaFragment = null;
    private JourneeFragment journeeFragment = null;

    private Context context;
    private GoogleCloudMessaging gcm;
    private String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accueil);
        DataSingleton.initialize();

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ParamsDownloader.update(((HOFCApplication)getApplication()).getRequestQueue());
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            mTitle = getText(R.string.title_accueil);
            getSupportActionBar().setTitle(mTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerToggle = setupDrawerToggle();
        mDrawerLayout.setDrawerListener(drawerToggle);

        if(savedInstanceState == null) {
            // Démarrage de l'application, mise en place du fragment Accueil
            if(this.fragmentManager == null)
                fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ActusFragment.newInstance())
                    .commit();
        }
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

            DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageForEmptyUri(R.drawable.no_image_available)
                    .showImageOnFail(R.drawable.no_image_available)
                    .build();

            ImageLoaderConfiguration loaderConfiguration = new ImageLoaderConfiguration.Builder(this)
                    .diskCache(new UnlimitedDiskCache(cacheDir))
                    .defaultDisplayImageOptions(displayImageOptions)
                    .build();

            ImageLoader.getInstance().init(loaderConfiguration);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataSingleton.closeAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        handleMenuItemClick(menuItem);
                        return true;
                    }
                });
    }

    private void handleMenuItemClick(MenuItem menuItem) {
        Class fragmentClass = null;
        Fragment fragment;
        if(this.fragmentManager == null)
            fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.navigation_item_accueil:
                fragmentClass = ActusFragment.class;
                mTitle = getText(R.string.title_accueil);
                break;
            case R.id.navigation_item_classement:
                fragmentClass = ClassementFragment.class;
                mTitle = getText(R.string.title_classement);
                break;
            case R.id.navigation_item_calendrier:
                fragmentClass = CalendrierFragment.class;
                mTitle = getText(R.string.title_calendrier);
                break;
            case R.id.navigation_item_agenda:
                fragmentClass = AgendaFragment.class;
                mTitle = getText(R.string.title_agenda);
                break;
            case R.id.navigation_item_journee:
                fragmentClass = JourneeFragment.class;
                mTitle = getText(R.string.title_journee);
                break;
        }
        if(fragmentClass != null) {
            try {
                fragment = (Fragment)fragmentClass.newInstance();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(mTitle);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,  R.string.navigation_drawer_close);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                    regId = gcm.register(AppConstant.SENDER_ID);
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
                String url = HOFCUtils.buildUrl(ServerConstant.NOTIF_CONTEXT, null);

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                    nameValuePairs.add(new BasicNameValuePair("notification_id", regId));
                    nameValuePairs.add(new BasicNameValuePair("uuid", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    httpClient.execute(httpPost);
                } catch (IOException ex) {
                    Log.e(Accueil.class.getName(), "Problem while sending Notification ID", ex);
                }
                return null;
            }
        }.execute(null, null, null);
    }
}
