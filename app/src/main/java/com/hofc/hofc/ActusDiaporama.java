package com.hofc.hofc;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.hofc.hofc.adapter.DiaporamaAdapter;
import com.hofc.hofc.data.DataSingleton;


public class ActusDiaporama extends ActionBarActivity {

    private String url;
    private int initialPosition;
    private MaterialDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actus_diaporama);
        dialog = new MaterialDialog.Builder(this)
                                   .theme(Theme.LIGHT)
                                   .content(R.string.loading_popup)
                                   .progress(true, 0)
                                   .build();
        dialog.show();
        Toolbar toolbar = (Toolbar)findViewById(R.id.diaporama_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOFC");

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
        dialog.dismiss();
        pager.setVisibility(View.VISIBLE);
    }
}
