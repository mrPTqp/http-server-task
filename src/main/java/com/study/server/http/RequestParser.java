package com.study.server.http;

import java.io.IOException;

public interface RequestParser {
    boolean parse(String inputLine) throws IOException;
}