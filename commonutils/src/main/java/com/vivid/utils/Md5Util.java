package com.vivid.utils;

import android.content.Context;
import android.os.Process;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by sean on 6/7/16.
 */
public class Md5Util {
    public interface OnGetMd5 {
        void onGetMd5(String md5);
    }
    public static void calculateApkMd5Async(Context appCtx, final OnGetMd5 callback) {
        if (appCtx == null || callback == null) {
            throw new InvalidParameterException("appCtx or callback should not be null!");
        }
        try {
            String packagePath = appCtx.getPackageCodePath();
            calculateMd5Async(new File(packagePath), callback);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onGetMd5(null);
        }
    }

    public static void calculateMd5Async(final File updateFile,
                                           final OnGetMd5 callback) {
        if (updateFile == null || callback == null) {
            throw new InvalidParameterException("file or callback should not be null!");
        }
        try {
            new Thread() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    try {
                        String md5 = calculateMd5(updateFile);
                        callback.onGetMd5(md5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onGetMd5(null);
                    }
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
            callback.onGetMd5(null);
        }
    }
    public static String calculateMd5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5");
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
