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

public class HashMapDataSingleton<T, V extends CommonBDD> {
    private static Map<Class<?>, HashMapDataSingleton> INSTANCE_MAP = new HashMap<>();
    private Map<String,List<T>> map;
    private Date lastSynchro;
    private V bdd;

    public static <T,V extends CommonBDD> HashMapDataSingleton<T,V> getInstance(Class<T> valueClass,Class<V> databaseClass) {
        HashMapDataSingleton<T,V> instance;
        if(INSTANCE_MAP.containsKey(valueClass)) {
            instance = (HashMapDataSingleton<T,V>) INSTANCE_MAP.get(valueClass);
        } else {
            instance = new HashMapDataSingleton<T,V>();
            INSTANCE_MAP.put(valueClass, instance);
        }
        if(instance.bdd == null) {
            try {
                instance.bdd = databaseClass.getDeclaredConstructor(Context.class).newInstance(HOFCApplication.get());
                instance.map = new HashMap<String, List<T>>();
                instance.lastSynchro = instance.bdd.getDateSynchro();
            } catch (InstantiationException e) {
                Log.e(HashMapDataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (IllegalAccessException e) {
                Log.e(HashMapDataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (NoSuchMethodException e) {
                Log.e(HashMapDataSingleton.class.getName(), "Error while creating database instance", e);
            } catch (InvocationTargetException e) {
                Log.e(HashMapDataSingleton.class.getName(), "Error while creating database instance", e);
            }
        }
        return instance;
    }

    public List<T> get(String key) {
        if(!map.containsKey(key)) {
            map.put(key, this.bdd.getWithKey(key));
        }
        return map.get(key);
    }

    public void set(String key, List<T> pList) {
        map.put(key, pList);
        bdd.insertListWithKey(key, pList);
    }

    public void handleDownload(String key, List<T> list) {
        this.set(key, list);
        this.updateDateSynchro(new Date());
    }

    public boolean isSynchroNeeded() {
    	GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    	calendar.setTime(new Date());
    	calendar.add(Calendar.DATE, -ServerConstant.NOMBRE_JOUR_SYNCHRO);
    	return (map == null || lastSynchro == null || lastSynchro.before(calendar.getTime()));
    }

    public void updateDateSynchro(Date date) {
        lastSynchro = date;
        bdd.updateDateSynchro(date);
    }
}
