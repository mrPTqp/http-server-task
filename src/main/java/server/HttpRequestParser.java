package server;

import java.io.BufferedReader;
import java.io.IOException;

public interface HttpRequestParser {
    boolean parse(BufferedReader in) throws IOException;
}
