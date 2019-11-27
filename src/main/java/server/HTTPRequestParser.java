package server;

import java.io.BufferedReader;
import java.io.IOException;

public interface HTTPRequestParser {
    boolean parse(BufferedReader in) throws IOException;
}
