package com.hofc.hofc.data;

import android.content.Context;

import com.hofc.hofc.constant.ServerConstant;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DataSingleton<T, V extends CommonBDD> {
	private static DataSingleton INSTANCE = new DataSingleton();
    private List<T> list;
    private Date lastSynchro;
    private V bdd;
    private Context c;

    public static <T,V extends CommonBDD> DataSingleton<T,V> getInstance(Class<T> valueClass,Class<V> databaseClass) {
        if(INSTANCE == null) {
            INSTANCE = new DataSingleton<T,V>();
        }
        return INSTANCE;
    }

    public void initialize(Context c) {
        try {
            bdd = (V)bdd.getClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void closeAll() {
        if(bdd != null)
            bdd.close();
    }

    public List<T> get() {
        return list;
    }

    public void set(List<T> pList) {
        list = pList;
        bdd.insertList(pList);
    }

    public boolean isSynchroNeeded() {
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
    	return (list == null || lastSynchro == null || lastSynchro.before(calendar.getTime()));
    }

    public void updateDateSynchro(Date date) {
        lastSynchro = date;
        bdd.updateDateSynchro(date);
    }
}
