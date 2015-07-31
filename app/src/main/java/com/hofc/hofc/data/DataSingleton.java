package com.hofc.hofc.data;

import android.content.Context;

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
        DataSingleton<T,V> instance = null;
        if(INSTANCE_MAP.containsKey(valueClass)) {
            instance = (DataSingleton<T,V>) INSTANCE_MAP.get(valueClass);
        } else {
            instance = new DataSingleton<T,V>();
            INSTANCE_MAP.put(valueClass, instance);
            return instance;
        }
        if(instance.bdd == null) {
            try {
                instance.bdd = databaseClass.getDeclaredConstructor(Context.class).newInstance(HOFCApplication.get());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return instance;
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
