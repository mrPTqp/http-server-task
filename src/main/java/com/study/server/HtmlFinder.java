package com.study.server;

import java.io.IOException;
import java.util.List;

public interface HtmlFinder {
    List<String> findHtmlInDir() throws IOException;
}