package com.study.server.utils;

import java.io.InputStream;

public class TestUtils {
    public static InputStream readFile(String name) {
        return TestUtils.class.getClassLoader().getResourceAsStream(name);
    }
}
