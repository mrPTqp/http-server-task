package com.study.server.http;

import java.io.InputStream;
import java.util.Optional;

public interface RequestParser {
    Optional<Request> parse(InputStream in);
}