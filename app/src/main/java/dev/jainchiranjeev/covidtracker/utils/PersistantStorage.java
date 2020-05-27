package dev.jainchiranjeev.covidtracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PersistantStorage {
    private static PersistantStorage instance = null;
    private static SharedPreferences persistentData;
    private static SharedPreferences.Editor persistentDataEditor;
    private static Context context = null;

    public static PersistantStorage getInstance(Context context) {
        if(instance == null) {
            synchronized (PersistantStorage.class) {
                instance = new PersistantStorage();
                instance.context = context;
                initializePersistantStorage();
            }
        }
        return instance;
    }

    private PersistantStorage() {
    }

    public static void initializePersistantStorage() {
        persistentData = context.getSharedPreferences("PersistentData", Context.MODE_PRIVATE);
    }

    public boolean storeStringValue(String key, String value) {
        //Store data in persistent storage for later use.
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.putString(key, value);
        persistentDataEditor.apply();
        return true;
    }

    public String getStringValue(String key) {
//      Get data from persistent stroage
        return persistentData.getString(key,null);
    }

    public boolean storeBooleanValue(String key, boolean value) {
        //Store data in persistent storage for later use.
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.putBoolean(key, value);
        persistentDataEditor.apply();
        return true;
    }

    public boolean getBooleanValue(String key) {
//      Get data from persistent stroage
        return persistentData.getBoolean(key,false);
    }

    public boolean storeIntValue(String key, int value) {
        //Store data in persistent storage for later use.
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.putInt(key, value);
        persistentDataEditor.apply();
        return true;
    }

    public int getIntValue(String key) {
//      Get data from persistent stroage
        return persistentData.getInt(key,0);
    }
}
