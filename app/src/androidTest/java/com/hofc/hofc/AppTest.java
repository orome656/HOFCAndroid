package com.hofc.hofc;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hofc.hofc.R;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.HOFCOpenHelper;
import com.hofc.hofc.data.download.DataDownloader;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.vo.ActuVO;
import com.hofc.hofc.vo.CalendrierLineVO;
import com.hofc.hofc.vo.ClassementLineVO;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * Classe de test de l'application
 * Created by maladota on 25/02/2015.
 */
public class AppTest extends AndroidTestCase {
    private String readFile(int id) throws java.io.IOException{
        InputStream calendrierData = getContext().getResources().openRawResource(id);
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

    private final CountDownLatch lock = new CountDownLatch(1);

    /**
     * Test d'appel réseau pour Actu sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testActusOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.actusdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.ACTUS_CONTEXT, null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ActuVO.class, ActusBDD.class);
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("actus", null, null);
        //database.close();
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.calendrierdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, CalendrierLineVO.class, CalendrierBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("calendrier", null, null);
        //database.close();

    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.classementdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ClassementLineVO.class, ClassementBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("classement", null, null);
        //database.close();

    }

    /**
     * Test d'appel réseau KO pour Actus sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testActusKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse().setBody("").setStatus("404"));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.ACTUS_CONTEXT, null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(true);
                lock.countDown();
            }
        }, ActuVO.class, ActusBDD.class);
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("actus", null, null);
        //database.close();

    }

    /**
     * Test d'appel réseau KO pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("").setStatus("404"));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(true);
                lock.countDown();
            }
        }, CalendrierLineVO.class, CalendrierBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("calendrier", null, null);
        //database.close();

    }

    /**
     * Test d'appel réseau KO pour Classement sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("").setStatus("404"));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(true);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(true);
                lock.countDown();
            }
        }, ClassementLineVO.class, ClassementBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("classement", null, null);
        //database.close();

    }

    /**
     * Test de l'insertion des données dans DataSingleton pour Actus
     * @throws Exception
     */
    public void testActusDataSingleton() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.actusdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.ACTUS_CONTEXT, null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(DataSingleton.getInstance(ActuVO.class, ActusBDD.class).get().size(), 2);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ActuVO.class, ActusBDD.class);
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("actus", null, null);
        //database.close();
    }

    /**
     * Test de l'insertion des données dans la base de données pour Actus
     * @throws Exception
     */
    public void testActusDatabase() throws Exception {
        final ActusBDD actusBDD = new ActusBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.actusdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.ACTUS_CONTEXT, null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(actusBDD.getAll().size(), 2);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ActuVO.class, ActusBDD.class);
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("actus", null, null);
        //database.close();
    }

    /**
     * Vérification de l'insertion dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierDataSingleton() throws Exception {
        final CalendrierBDD calendrierBDD = new CalendrierBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse().setBody(readFile(R.raw.calendrierdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(calendrierBDD.getAll().size(), 3);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, CalendrierLineVO.class, CalendrierBDD.class, "equipe1");
        lock.await();
        server.shutdown();

        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("calendrier", null, null);
        //database.close();

    }

    /**
     * Vérification de l'insertion dans la base de données Calendrier
     * @throws Exception
     */
    public void testCalendrierDatabase() throws Exception {
        final CalendrierBDD calendrierBDD = new CalendrierBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.calendrierdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(calendrierBDD.getAll().size(), 3);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, CalendrierLineVO.class, CalendrierBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("calendrier", null, null);
        //database.close();
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementDataSingleton() throws Exception {
        final ClassementBDD classementBDD = new ClassementBDD(getContext());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.classementdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(classementBDD.getAll().size(), 3);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ClassementLineVO.class, ClassementBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("classement", null, null);
        //database.close();
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementDatabase() throws Exception {
        final ClassementBDD classementBDD = new ClassementBDD(getContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody(readFile(R.raw.classementdata)));
        server.start(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.downloadWithKey(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(classementBDD.getAll().size(), 3);
                lock.countDown();
            }

            @Override
            public void onError() {
                assertTrue(false);
                lock.countDown();
            }

            @Override
            public void onError(int messageId) {
                assertTrue(false);
                lock.countDown();
            }
        }, ClassementLineVO.class, ClassementBDD.class, "equipe1");
        lock.await();
        server.shutdown();
        SQLiteDatabase database = HOFCOpenHelper.getInstance(getContext().getApplicationContext()).getWritableDatabase();
        database.delete("classement", null, null);
        //database.close();
        //getContext().deleteDatabase("hofc.db");
    }
}
