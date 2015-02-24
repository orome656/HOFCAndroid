package com.hofc.hofc.data;

import android.content.Context;
import android.graphics.Bitmap;

import com.hofc.hofc.constant.ServerConstant;
import com.hofc.hofc.vo.ActuVO;
import com.hofc.hofc.vo.CalendrierLineVO;
import com.hofc.hofc.vo.ClassementLineVO;

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

    private Map<String, Bitmap> cachedImage;

    public static void initialize(Context c) {
        INSTANCE.cachedImage = new HashMap<>();
    	
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

    public static void insertImageCache(String url, Bitmap image) {
        INSTANCE.cachedImage.put(url, image);
    }

    public static Bitmap getCachedImage(String url) {
        if(INSTANCE.cachedImage.containsKey(url)) {
            return INSTANCE.cachedImage.get(url);
        } else {
            return null;
        }
    }
}
