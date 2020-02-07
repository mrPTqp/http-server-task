package com.study.server.utils;

import java.util.regex.Pattern;

public class HttpPatternsUtils {
    public static final Pattern MAIN_STRING = Pattern.compile(
            "(?<method>[\\x41-\\x5A]+)( )((?<path>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[./]]]]+)"
                    + "((\\?)(?<parameters>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[,.=&]]]]+))?)? "
                    + "(?<protocol>HTTP/[\\d].[\\d])"
    );
    public static final Pattern PAIRS_PATTERN = Pattern.compile(
            "(?<key>[a-zA-Z\\d]+)=(?<value>[a-zA-Z\\d]+)"
    );
    public static final Pattern HEADERS_PATTERN = Pattern.compile(
            "(?<key>[\\x20-\\x7D&&[^:]]+)(: )(?<value>[\\x20-\\x7D]+)"
    );
    public static final Pattern HOST_PATTERN = Pattern.compile(
            "(?<host>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E[^:]]]]]+)(:)?(?<port>\\d+)?"
    );

    public static final Pattern PATH_HOST_PATTERN = Pattern.compile(
            "(.+\\\\)(?<pathHost>[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E[^:]]]]]+\\."
                    + "[\\x41-\\x5A[\\x61-\\x7A[\\x30-\\x39[\\x2D-\\x2E]]]]+)"
    );
}
