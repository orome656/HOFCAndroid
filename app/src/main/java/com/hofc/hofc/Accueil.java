package com.hofc.hofc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.hofc.hofc.data.DataSingleton;

import java.io.IOException;

public class Accueil extends Activity
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
    private MenuItem refreshButton = null;
    private Menu menu = null;
    private boolean runningDownload = false;

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
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        /**
         *
         */
        context = getApplicationContext();
        gcm = GoogleCloudMessaging.getInstance(context);
        regId = GcmPreference.getRegistrationId(context);
        if(regId == null || regId.isEmpty()) {
            registerInBackground();
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
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_accueil);
                break;
            case 2:
                mTitle = getString(R.string.title_classement);
                break;
            case 3:
                mTitle = getString(R.string.title_calendrier);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
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
        if(this.runningDownload) {
            if(menu.findItem(R.id.action_refresh) != null)
                startRefreshAnimation(menu.findItem(R.id.action_refresh));
        }
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == R.id.action_refresh) {
        	CustomFragment custom = (CustomFragment) fragmentManager.findFragmentById(R.id.container);
            this.startRefreshAnimation(item);
        	custom.refreshDataAndView();
        }
        return super.onOptionsItemSelected(item);
    }

    public void startRefresh() {
        this.runningDownload = true;
        if (this.menu != null) {
            MenuItem item = this.menu.findItem(R.id.action_refresh);
            if (item != null) {
                this.startRefreshAnimation(item);
            }
        }
    }

    private void startRefreshAnimation(MenuItem refreshItem) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh, null);
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        refreshItem.setActionView(iv);
        this.refreshButton = refreshItem;
    }

    public void endRefresh() {
        this.runningDownload = false;
        if(this.refreshButton != null && this.refreshButton.getActionView() != null) {
            this.refreshButton.getActionView().clearAnimation();
            this.refreshButton.setActionView(null);
        }
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
                String msg = "";
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
    }
}
