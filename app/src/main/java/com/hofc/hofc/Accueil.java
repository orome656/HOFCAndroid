package com.hofc.hofc;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.hofc.hofc.data.DataSingleton;

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_refresh) {
        	CustomFragment custom = (CustomFragment) fragmentManager.findFragmentById(R.id.container);
        	custom.refreshDataAndView();
        }
        return super.onOptionsItemSelected(item);
    }
}
