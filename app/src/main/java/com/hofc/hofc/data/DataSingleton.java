package com.hofc.hofc.data;

import android.content.Context;

import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.vo.ActuVO;
import com.hofc.hofc.vo.AgendaLineVO;
import com.hofc.hofc.vo.CalendrierLineVO;
import com.hofc.hofc.vo.ClassementLineVO;
import com.hofc.hofc.vo.MatchVO;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DataSingleton {
	INSTANCE;
    private List<CalendrierLineVO> calendrier;
    private Date lastSynchroCalendrier;
    private List<ClassementLineVO> classement;
    private Date lastSynchroClassement;
    private List<ActuVO> actus;
    private Date lastSynchroActus;
    private Map<String,List<AgendaLineVO>> agenda;
    private Map<String,List<MatchVO>> journee;
    private Params params;

    private HashMap<String, List<String>> cacheImageUrls;

    public static void initialize() {
        INSTANCE.cacheImageUrls = new HashMap<>();
        INSTANCE.agenda = new HashMap<>();
        INSTANCE.journee = new HashMap<>();
        INSTANCE.params = new Params();
    }

    public static void initializeCalendrier(Context c) {
        CalendrierBDD.initiate(c);
        INSTANCE.calendrier = CalendrierBDD.getAll();
        INSTANCE.lastSynchroCalendrier = CalendrierBDD.getDateSynchro();

    }


    public static void initializeClassement(Context c) {
        ClassementBDD.initiate(c);
        INSTANCE.classement = ClassementBDD.getAll();
        INSTANCE.lastSynchroClassement = ClassementBDD.getDateSynchro();

    }


    public static void initializeActus(Context c) {
        ActusBDD.initiate(c);
        INSTANCE.actus = ActusBDD.getAll();
        INSTANCE.lastSynchroActus = ActusBDD.getDateSynchro();

    }
    
    public static List<CalendrierLineVO> getCalendrier() {
    	return INSTANCE.calendrier;
    }
    
    public static void setCalendrier(List<CalendrierLineVO> pCalendrier) {
    	INSTANCE.calendrier = pCalendrier;
    }
    
    public static List<ClassementLineVO> getClassement() {
        return INSTANCE.classement;
    }

    public static void setClassement(List<ClassementLineVO> pClassement) {
    	INSTANCE.classement = pClassement;
    }
    
    public static List<ActuVO> getActus() {
        return INSTANCE.actus;
    }

    public static void setActus(List<ActuVO> pActus) {
    	INSTANCE.actus = pActus;
    }

    public static List<AgendaLineVO> getAgenda(String date) { return INSTANCE.agenda.get(date); }

    public static void setAgenda(String date,List<AgendaLineVO> list) { INSTANCE.agenda.put(date,list); }

    public static List<MatchVO> getJournee(String journeeId) { return INSTANCE.journee.get(journeeId); }

    public static void setJournee(String journeeId,List<MatchVO> list) { INSTANCE.journee.put(journeeId, list); }

    public static boolean isSynchroCalendrierNeeded() {
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
    	return (INSTANCE.calendrier == null || INSTANCE.lastSynchroCalendrier == null || INSTANCE.lastSynchroCalendrier.before(calendar.getTime()));
    }

    public static boolean isSynchroClassementNeeded() {
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
    	return (INSTANCE.classement == null || INSTANCE.lastSynchroClassement == null || INSTANCE.lastSynchroClassement.before(calendar.getTime()));
    }

    public static boolean isSynchroActuNeeded() {
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
    	return (INSTANCE.actus == null || INSTANCE.lastSynchroActus == null || INSTANCE.lastSynchroActus.before(calendar.getTime()));
    }

    public static void updateDateSynchroClassement(Date date) {
        INSTANCE.lastSynchroClassement = date;
    }

    public static void updateDateSynchroCalendrier(Date date) {
        INSTANCE.lastSynchroCalendrier = date;
    }

    public static void updateDateSynchroActus(Date date) {
        INSTANCE.lastSynchroActus = date;
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
}
