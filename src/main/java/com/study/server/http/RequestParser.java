package com.study.server.http;

import java.io.InputStream;

public interface RequestParser {
    Request parse(InputStream in);
}