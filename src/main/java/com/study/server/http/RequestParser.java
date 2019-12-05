package com.study.server.http;

import java.io.IOException;

public interface RequestParser {
    Request parse(String inputLine) throws IOException;
}
