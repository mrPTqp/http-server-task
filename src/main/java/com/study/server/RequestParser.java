package com.study.server;

import java.io.BufferedReader;
import java.io.IOException;

public interface RequestParser {
    boolean parse(BufferedReader in) throws IOException;
}
