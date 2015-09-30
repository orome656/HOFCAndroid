package com.hofc.hofc.ui;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;

import com.hofc.hofc.ActusDiaporama;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
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
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.swipeLeft;

/**
 * Created by Anthony on 24/09/2015.
 */
@RunWith(AndroidJUnit4.class)
public class DiaporamaTest extends ActivityInstrumentationTestCase2<ActusDiaporama> {
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

    public DiaporamaTest() throws Exception {
        super(ActusDiaporama.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Intent i = new Intent();
        i.putExtra("URL", "http://www.hofc.fr/2015/09/hofc-ii-haut-adour-iii-en-images-2/");
        i.putExtra("position", 1);
        setActivityIntent(i);
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
                        } else if (request.getPath().contains("/parsePage")) {
                            return new MockResponse().setResponseCode(200).setBody(readFile(R.raw.diaporamadata));
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
        getActivity();
    }

    @Test
    public void testInit() {
        ActusDiaporama activity = getActivity();
        assertNotNull(activity);
    }


    @Test
    public void testInitDiapo() {
        ActusDiaporama activity = getActivity();
        onView(withId(R.id.view_pager)).perform(swipeLeft());
    }

    public void testImageNumber() {
        ActusDiaporama activity = getActivity();
        ViewPager viewPager = (ViewPager)activity.findViewById(R.id.view_pager);
        assertEquals(12, viewPager.getAdapter().getCount());
    }


    public void testImagePosition() {
        ActusDiaporama activity = getActivity();
        ViewPager viewPager = (ViewPager)activity.findViewById(R.id.view_pager);
        assertEquals(1, viewPager.getCurrentItem()
        );
    }

    @AfterClass
    public static void end() throws Exception {
        server.shutdown();
    }
}
