package com.hofc.hofc.ui;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.hofc.hofc.Accueil;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.fragment.ActusFragment;
import com.squareup.okhttp.mockwebserver.Dispatcher;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.DrawerActions.closeDrawer;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Anthony on 18/09/2015.
 */
@RunWith(AndroidJUnit4.class)
public class JourneeUnitTest extends ActivityInstrumentationTestCase2<Accueil> {
    private Accueil mActivity;
    private static MockWebServer server;

    private String readFile(int id) throws java.io.IOException{
        InputStream calendrierData = getActivity().getResources().openRawResource(id);
        InputStreamReader inputStreamReader = new InputStreamReader(calendrierData);
        BufferedReader r = new BufferedReader(inputStreamReader);
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        inputStreamReader.close();
        calendrierData.close();
        r.close();
        return total.toString();
    }

    public JourneeUnitTest() throws Exception {
        super(Accueil.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        if(server == null) {
            server = new MockWebServer();
            final Dispatcher dispatcher = new Dispatcher() {
                @Override
                public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                    try {
                        if (request.getPath().equals("/actus")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.actusdata));
                        } else if (request.getPath().equals("/calendrier/equipe1") || request.getPath().equals("/calendrier/equipe2") || request.getPath().equals("/calendrier/equipe3")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.calendrierdata));
                        } else if (request.getPath().equals("/classement/equipe1") || request.getPath().equals("/classement/equipe2") || request.getPath().equals("/classement/equipe3")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.classementdata));
                        } else if (request.getPath().contains("/agenda")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.agendadata));
                        } else if (request.getPath().contains("/journee")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.journeedata));
                        } else if (request.getPath().contains("/params")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.paramsdata));
                        }
                        return new MockResponse().setResponseCode(404);
                    } catch(Exception e) {
                        return new MockResponse().setResponseCode(404);
                    }
                }

            };
            server.setDispatcher(dispatcher);
            server.start(3000);
        }
        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

    }

    @Test
    public void testInitAccueil() {
        Accueil activity = getActivity();
        assertNotNull(activity);
    }

    @Test
    public void testAccueilFragment() {
        Accueil activity = getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        assertNotNull(fragment);
        assertTrue(fragment instanceof ActusFragment);
    }

    @Test
    public void testActusDisplayedNumber() throws Exception {
        Accueil activity = getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        ListView listActus = (ListView)fragment.getView().findViewById(R.id.actus_listview);
        assertEquals(listActus.getAdapter().getCount(), 2);
    }

    @Test
    public void testDrawer() {
        openDrawer(R.id.drawer_layout);
        closeDrawer(R.id.drawer_layout);
    }

    @Test
    public void changeToJourneeExcellence() {
        Accueil activity = getActivity();
        openDrawer(R.id.drawer_layout);
        onView(withText("Agenda Excellence")).perform(click());
    }


    @Test
    public void testJourneeExcellenceNumber() throws Exception {
        Accueil activity = getActivity();
        openDrawer(R.id.drawer_layout);
        onView(withText("Agenda Excellence")).perform(click());
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        ListView list = (ListView)fragment.getView().findViewById(R.id.journee_listView);
        assertEquals(list.getAdapter().getCount(), 6);
    }

    @AfterClass
    public static void end() throws Exception {
        server.shutdown();
    }
}
