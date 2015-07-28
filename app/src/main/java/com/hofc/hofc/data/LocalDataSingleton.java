package com.hofc.hofc.data;

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
    private Map<String,List<MatchVO>> journee;
    private Params params;

    private Map<String, List<String>> cacheImageUrls;

    public static void initialize() {
        INSTANCE.cacheImageUrls = new HashMap<>();
        INSTANCE.agenda = new HashMap<>();
        INSTANCE.journee = new HashMap<>();
        INSTANCE.params = new Params();
    }

    public static List<AgendaLineVO> getAgenda(String date) { return INSTANCE.agenda.get(date); }

    public static void setAgenda(String date,List<AgendaLineVO> list) { INSTANCE.agenda.put(date,list); }

    public static List<MatchVO> getJournee(String journeeId) { return INSTANCE.journee.get(journeeId); }

    public static void setJournee(String journeeId,List<MatchVO> list) { INSTANCE.journee.put(journeeId, list); }


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

}
