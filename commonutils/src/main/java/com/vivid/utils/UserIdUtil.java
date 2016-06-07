package com.vivid.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.UUID;
/**
 * Created by sean on 6/7/16.
 */
public class UserIdUtil {
    private static final String DEFAULT_VALUE = "NONE";

    public static String getUniqueUId(Context context) {
        final TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + getAndroidID(context);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    private static String getAndroidID(Context context) {
        String androidID = DEFAULT_VALUE;
        Class<?> cls = Settings.Secure.class;
        try {
            Field fld = cls.getDeclaredField("ANDROID_ID");
            if (fld == null) {
                return androidID;
            }
        } catch (NoSuchFieldException e) {
            return androidID;
        }
        return getAndroidIDNormal(context);
    }

    private static String getAndroidIDNormal(Context context) {
        try {
            String id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            return id;
        } catch (Throwable t) {
            return DEFAULT_VALUE;
        }
    }
}

