package com.hofc.test;

import android.test.AndroidTestCase;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.data.ActusBDD;
import com.hofc.hofc.data.CalendrierBDD;
import com.hofc.hofc.data.ClassementBDD;
import com.hofc.hofc.data.DataSingleton;
import com.hofc.hofc.data.download.DataDownloader;
import com.hofc.hofc.fragment.FragmentCallback;
import com.hofc.hofc.vo.ActuVO;
import com.hofc.hofc.vo.CalendrierLineVO;
import com.hofc.hofc.vo.ClassementLineVO;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import java.util.concurrent.CountDownLatch;

/**
 * Classe de test de l'application
 * Created by maladota on 25/02/2015.
 */
public class AppTest extends AndroidTestCase {
    private final CountDownLatch lock = new CountDownLatch(1);

    /**
     * Test d'appel réseau pour Actu sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testActusOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":19,\"postid\":\"24776\",\"titre\":\"Nouvelle Vague HOFC en images\",\"texte\":\"Bon anniversaire Jean-Paul , merci pour tout .\",\"url\":\"http://www.hofc.fr/2015/02/nouvelle-vague-hofc-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/02/HOFC-Nouvelle-vague-2-1-HOFC-14-02-2015-53.jpg&h=150&w=250&zc=1\",\"date\":\"2015-02-18T00:00:00.000Z\"},{\"id\":18,\"postid\":\"24419\",\"titre\":\"HOFC ARGELES en images\",\"texte\":\"Credit Photos : a Jean Paul LAMARQUE CHOY\",\"url\":\"http://www.hofc.fr/2015/01/hofc-argeles-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/01/HOFC-ARGELES-2-2-HOFC-24-01-2015-14.jpg&h=150&w=250&zc=1\",\"date\":\"2015-01-28T00:00:00.000Z\"}]"));
        server.play(3000);

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

        getContext().deleteDatabase("hofc.db");
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"equipe1\":\"HORGUES ODOS F.C.\",\"score1\":1,\"equipe2\":\"UST NOUV VAG.\",\"score2\":1,\"date\":\"2014-09-06T00:00:00.000Z\"},{\"id\":2,\"equipe1\":\"ELPY BBL 2\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-09-20T00:00:00.000Z\"},{\"id\":3,\"equipe1\":\"GUIZERIX\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-10-05T00:00:00.000Z\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
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
        }, CalendrierLineVO.class, CalendrierBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");

    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementOK() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"nom\":\"GUIZERIX\",\"points\":\"41\",\"joue\":\"12\",\"gagne\":\"9\",\"nul\":\"2\",\"perdu\":\"1\",\"bp\":\"32\",\"bc\":\"11\",\"diff\":\"21\"},{\"id\":2,\"nom\":\"BOUT D  OR GER\",\"points\":\"39\",\"joue\":\"12\",\"gagne\":\"8\",\"nul\":\"3\",\"perdu\":\"1\",\"bp\":\"20\",\"bc\":\"7\",\"diff\":\"13\"},{\"id\":3,\"nom\":\"HORGUES ODOS F.C.\",\"points\":\"36\",\"joue\":\"12\",\"gagne\":\"7\",\"nul\":\"3\",\"perdu\":\"2\",\"bp\":\"17\",\"bc\":\"9\",\"diff\":\"8\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
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
        }, ClassementLineVO.class, ClassementBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");

    }

    /**
     * Test d'appel réseau KO pour Actus sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testActusKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();

        server.enqueue(new MockResponse().setBody("[{\"id\":19,\"postid\":\"24776\",\"titre\":\"Nouvelle Vague HOFC en images\",\"texte\":\"Bon anniversaire Jean-Paul , merci pour tout .\",\"url\":\"http://www.hofc.fr/2015/02/nouvelle-vague-hofc-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/02/HOFC-Nouvelle-vague-2-1-HOFC-14-02-2015-53.jpg&h=150&w=250&zc=1\",\"date\":\"2015-02-18T00:00:00.000Z\"},{\"id\":18,\"postid\":\"24419\",\"titre\":\"HOFC ARGELES en images\",\"texte\":\"Credit Photos : a Jean Paul LAMARQUE CHOY\",\"url\":\"http://www.hofc.fr/2015/01/hofc-argeles-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/01/HOFC-ARGELES-2-2-HOFC-24-01-2015-14.jpg&h=150&w=250&zc=1\",\"date\":\"2015-01-28T00:00:00.000Z\"}]").setStatus("404"));
        server.play(3000);

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
        getContext().deleteDatabase("hofc.db");

    }

    /**
     * Test d'appel réseau KO pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"equipe1\":\"HORGUES ODOS F.C.\",\"score1\":1,\"equipe2\":\"UST NOUV VAG.\",\"score2\":1,\"date\":\"2014-09-06T00:00:00.000Z\"},{\"id\":2,\"equipe1\":\"ELPY BBL 2\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-09-20T00:00:00.000Z\"},{\"id\":3,\"equipe1\":\"GUIZERIX\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-10-05T00:00:00.000Z\"}]").setStatus("404"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
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
        }, CalendrierLineVO.class, CalendrierBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");

    }

    /**
     * Test d'appel réseau KO pour Classement sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementKO() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"nom\":\"GUIZERIX\",\"points\":\"41\",\"joue\":\"12\",\"gagne\":\"9\",\"nul\":\"2\",\"perdu\":\"1\",\"bp\":\"32\",\"bc\":\"11\",\"diff\":\"21\"},{\"id\":2,\"nom\":\"BOUT D  OR GER\",\"points\":\"39\",\"joue\":\"12\",\"gagne\":\"8\",\"nul\":\"3\",\"perdu\":\"1\",\"bp\":\"20\",\"bc\":\"7\",\"diff\":\"13\"},{\"id\":3,\"nom\":\"HORGUES ODOS F.C.\",\"points\":\"36\",\"joue\":\"12\",\"gagne\":\"7\",\"nul\":\"3\",\"perdu\":\"2\",\"bp\":\"17\",\"bc\":\"9\",\"diff\":\"8\"}]").setStatus("404"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
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
        }, ClassementLineVO.class, ClassementBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");

    }

    /**
     * Test de l'insertion des données dans DataSingleton pour Actus
     * @throws Exception
     */
    public void testActusDataSingleton() throws Exception {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":19,\"postid\":\"24776\",\"titre\":\"Nouvelle Vague HOFC en images\",\"texte\":\"Bon anniversaire Jean-Paul , merci pour tout .\",\"url\":\"http://www.hofc.fr/2015/02/nouvelle-vague-hofc-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/02/HOFC-Nouvelle-vague-2-1-HOFC-14-02-2015-53.jpg&h=150&w=250&zc=1\",\"date\":\"2015-02-18T00:00:00.000Z\"},{\"id\":18,\"postid\":\"24419\",\"titre\":\"HOFC ARGELES en images\",\"texte\":\"Credit Photos : a Jean Paul LAMARQUE CHOY\",\"url\":\"http://www.hofc.fr/2015/01/hofc-argeles-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/01/HOFC-ARGELES-2-2-HOFC-24-01-2015-14.jpg&h=150&w=250&zc=1\",\"date\":\"2015-01-28T00:00:00.000Z\"}]"));
        server.play(3000);

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
        getContext().deleteDatabase("hofc.db");
    }

    /**
     * Test de l'insertion des données dans la base de données pour Actus
     * @throws Exception
     */
    public void testActusDatabase() throws Exception {
        final ActusBDD actusBDD = new ActusBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":19,\"postid\":\"24776\",\"titre\":\"Nouvelle Vague HOFC en images\",\"texte\":\"Bon anniversaire Jean-Paul , merci pour tout .\",\"url\":\"http://www.hofc.fr/2015/02/nouvelle-vague-hofc-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/02/HOFC-Nouvelle-vague-2-1-HOFC-14-02-2015-53.jpg&h=150&w=250&zc=1\",\"date\":\"2015-02-18T00:00:00.000Z\"},{\"id\":18,\"postid\":\"24419\",\"titre\":\"HOFC ARGELES en images\",\"texte\":\"Credit Photos : a Jean Paul LAMARQUE CHOY\",\"url\":\"http://www.hofc.fr/2015/01/hofc-argeles-en-images/\",\"image\":\"http://www.hofc.fr/wp-content/themes/Canyon/timthumb.php?src=http://www.hofc.fr/wp-content/uploads/2015/01/HOFC-ARGELES-2-2-HOFC-24-01-2015-14.jpg&h=150&w=250&zc=1\",\"date\":\"2015-01-28T00:00:00.000Z\"}]"));
        server.play(3000);

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
        getContext().deleteDatabase("hofc.db");
    }

    /**
     * Vérification de l'insertion dans DataSingleton
     * @throws Exception
     */
    public void testCalendrierDataSingleton() throws Exception {
        final CalendrierBDD calendrierBDD = new CalendrierBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"equipe1\":\"HORGUES ODOS F.C.\",\"score1\":1,\"equipe2\":\"UST NOUV VAG.\",\"score2\":1,\"date\":\"2014-09-06T00:00:00.000Z\"},{\"id\":2,\"equipe1\":\"ELPY BBL 2\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-09-20T00:00:00.000Z\"},{\"id\":3,\"equipe1\":\"GUIZERIX\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-10-05T00:00:00.000Z\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(calendrierBDD.getAll().size(),3);
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
        }, CalendrierLineVO.class, CalendrierBDD.class);
        lock.await();
        server.shutdown();

    }

    /**
     * Vérification de l'insertion dans la base de données Calendrier
     * @throws Exception
     */
    public void testCalendrierDatabase() throws Exception {
        final CalendrierBDD calendrierBDD = new CalendrierBDD(getContext());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"equipe1\":\"HORGUES ODOS F.C.\",\"score1\":1,\"equipe2\":\"UST NOUV VAG.\",\"score2\":1,\"date\":\"2014-09-06T00:00:00.000Z\"},{\"id\":2,\"equipe1\":\"ELPY BBL 2\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-09-20T00:00:00.000Z\"},{\"id\":3,\"equipe1\":\"GUIZERIX\",\"score1\":0,\"equipe2\":\"HORGUES ODOS F.C.\",\"score2\":0,\"date\":\"2014-10-05T00:00:00.000Z\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CALENDRIER_CONTEXT[0], null, new FragmentCallback() {
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
        }, CalendrierLineVO.class, CalendrierBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementDataSingleton() throws Exception {
        final ClassementBDD classementBDD = new ClassementBDD(getContext());
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"nom\":\"GUIZERIX\",\"points\":\"41\",\"joue\":\"12\",\"gagne\":\"9\",\"nul\":\"2\",\"perdu\":\"1\",\"bp\":\"32\",\"bc\":\"11\",\"diff\":\"21\"},{\"id\":2,\"nom\":\"BOUT D  OR GER\",\"points\":\"39\",\"joue\":\"12\",\"gagne\":\"8\",\"nul\":\"3\",\"perdu\":\"1\",\"bp\":\"20\",\"bc\":\"7\",\"diff\":\"13\"},{\"id\":3,\"nom\":\"HORGUES ODOS F.C.\",\"points\":\"36\",\"joue\":\"12\",\"gagne\":\"7\",\"nul\":\"3\",\"perdu\":\"2\",\"bp\":\"17\",\"bc\":\"9\",\"diff\":\"8\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
            @Override
            public void onTaskDone() {
                assertEquals(classementBDD.getAll().size(),3);
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
        }, ClassementLineVO.class, ClassementBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");
    }

    /**
     * Test d'appel réseau pour Calendrier sans vérification d'insertion dans la base ou dans DataSingleton
     * @throws Exception
     */
    public void testClassementDatabase() throws Exception {
        final ClassementBDD classementBDD = new ClassementBDD(getContext());


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("[{\"id\":1,\"nom\":\"GUIZERIX\",\"points\":\"41\",\"joue\":\"12\",\"gagne\":\"9\",\"nul\":\"2\",\"perdu\":\"1\",\"bp\":\"32\",\"bc\":\"11\",\"diff\":\"21\"},{\"id\":2,\"nom\":\"BOUT D  OR GER\",\"points\":\"39\",\"joue\":\"12\",\"gagne\":\"8\",\"nul\":\"3\",\"perdu\":\"1\",\"bp\":\"20\",\"bc\":\"7\",\"diff\":\"13\"},{\"id\":3,\"nom\":\"HORGUES ODOS F.C.\",\"points\":\"36\",\"joue\":\"12\",\"gagne\":\"7\",\"nul\":\"3\",\"perdu\":\"2\",\"bp\":\"17\",\"bc\":\"9\",\"diff\":\"8\"}]"));
        server.play(3000);

        ServerConstant.SERVER_URL_PREFIX = server.getUrl("").getProtocol() + "://";
        ServerConstant.SERVER_URL = server.getUrl("").getHost();
        ServerConstant.SERVER_PORT = 3000;

        DataDownloader.download(requestQueue, ServerConstant.CLASSEMENT_CONTEXT[0], null, new FragmentCallback() {
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
        }, ClassementLineVO.class, ClassementBDD.class);
        lock.await();
        server.shutdown();
        getContext().deleteDatabase("hofc.db");
    }
}
