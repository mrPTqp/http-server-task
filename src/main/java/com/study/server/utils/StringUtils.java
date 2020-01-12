package com.study.server.utils;

public class StringUtils {
    public static boolean isEmpty(String instance) {
        if (instance == null || instance.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
