package com.onemoreerror.sampletask.PreferenceUtil;


import android.content.Context;
import android.content.SharedPreferences;

public class ZPreferences {

    private static final String KEY = "mygate.prefs";

    private static final String FIRST_LAUNCH = "IS_USER_LOGIN";

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return savedSession.getBoolean(FIRST_LAUNCH, true);
    }
    public static void setIsFirstLaunch(Context context,boolean isFirst) {
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(FIRST_LAUNCH, isFirst);
        editor.apply();

    }

}
