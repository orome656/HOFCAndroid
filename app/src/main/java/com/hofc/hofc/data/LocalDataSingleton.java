package com.hofc.hofc.data;

import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.vo.AgendaLineVO;
import com.hofc.hofc.vo.MatchVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by antho on 28/07/15.
 */
public enum LocalDataSingleton {
    INSTANCE;
    private Map<String,List<AgendaLineVO>> agenda;
    private Map<String,Map<String,List<MatchVO>>> journee;
    private JourneeBdd journeeBdd;
    private AgendaBDD agendaBdd;

    private ParamsBDD paramsBdd;
    private Params params;

    private Map<String, List<String>> cacheImageUrls;

    public static void initialize() {
        INSTANCE.cacheImageUrls = new HashMap<>();
        INSTANCE.agenda = new HashMap<>();
        INSTANCE.journee = new HashMap<>();

        INSTANCE.agendaBdd = new AgendaBDD(HOFCApplication.get());
        INSTANCE.journeeBdd = new JourneeBdd(HOFCApplication.get());
        INSTANCE.paramsBdd = new ParamsBDD(HOFCApplication.get());
        INSTANCE.params = INSTANCE.paramsBdd.getParams();
    }

    public static List<AgendaLineVO> getAgenda(String date) {
        if(INSTANCE.agenda.get(date)==null) {
            INSTANCE.agenda.put(date, INSTANCE.agendaBdd.getAll(date));
        }
        return INSTANCE.agenda.get(date);
    }

    public static void setAgenda(String date,List<AgendaLineVO> list) {
        INSTANCE.agenda.put(date,list);
        INSTANCE.agendaBdd.insertList(date, list);
    }

    public static List<MatchVO> getJournee(String journeeId, String equipe) {
        if(INSTANCE.journee.get(equipe) == null) {
            INSTANCE.journee.put(equipe, new HashMap<String, List<MatchVO>>());
        }
        if(INSTANCE.journee.get(equipe).get(journeeId) == null) {
            INSTANCE.journee.get(equipe).put(journeeId, INSTANCE.journeeBdd.getAll(journeeId, equipe));
        }
        return INSTANCE.journee.get(equipe).get(journeeId);
    }

    public static void setJournee(String journeeId, String equipe, List<MatchVO> list) {
        if(INSTANCE.journee.get(equipe) == null) {
            INSTANCE.journee.put(equipe, new HashMap<String, List<MatchVO>>());
        }

        INSTANCE.journee.get(equipe).put(journeeId, list);
        INSTANCE.journeeBdd.insertList(journeeId, equipe, list);
    }


    public static void insertImageCacheUrls(String url, List<String> urls) {
        INSTANCE.cacheImageUrls.put(url, urls);
    }

    public static List<String> getCachedImageUrls(String url) {
        if(INSTANCE.cacheImageUrls.containsKey(url)) {
            return INSTANCE.cacheImageUrls.get(url);
        } else {
            return null;
        }
    }

    public static Params getParams() {
        return INSTANCE.params;
    }

    public static void addParam(String name, String value) {
        INSTANCE.paramsBdd.saveParam(name, value);
    }

}
