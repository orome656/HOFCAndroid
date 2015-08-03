package com.hofc.hofc.data;

import android.content.Context;
import android.util.Log;

import com.hofc.hofc.HOFCApplication;
import com.hofc.hofc.constant.ServerConstant;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSingleton<T, V extends CommonBDD> {
    private static Map<Class<?>, DataSingleton> INSTANCE_MAP = new HashMap<>();
    private List<T> list;
    private Date lastSynchro;
    private V bdd;

    public static <T,V extends CommonBDD> DataSingleton<T,V> getInstance(Class<T> valueClass,Class<V> databaseClass) {
        DataSingleton<T,V> instance;
        if(INSTANCE_MAP.containsKey(valueClass)) {
            instance = (DataSingleton<T,V>) INSTANCE_MAP.get(valueClass);
        } else {
            instance = new DataSingleton<T,V>();
            INSTANCE_MAP.put(valueClass, instance);
        }
        if(instance.bdd == null) {
            try {
                instance.bdd = databaseClass.getDeclaredConstructor(Context.class).newInstance(HOFCApplication.get());
                instance.list = instance.bdd.getAll();
                instance.lastSynchro = instance.bdd.getDateSynchro();
            } catch (InstantiationException e) {
                Log.e(DataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (IllegalAccessException e) {
                Log.e(DataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (NoSuchMethodException e) {
                Log.e(DataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (InvocationTargetException e) {
                Log.e(DataSingleton.class.getName(), "Error while creating database instance", e);
            }
        }
        return instance;
    }

    public List<T> get() {
        return list;
    }

    public void set(List<T> pList) {
        list = pList;
        bdd.insertList(pList);
    }

    public void handleDownload(List<T> list) {
        this.set(list);
        this.updateDateSynchro(new Date());
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
