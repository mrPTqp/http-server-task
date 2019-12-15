package com.study.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlFinderImpl implements HtmlFinder {
    private String rootDir;
    private String fileName = "index.html";
    private List<String> indexHtmlFilesPaths;

    public HtmlFinderImpl(String rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public List<String> findHtmlInDir() {
        Path dir = Paths.get(rootDir);

        try (Stream<Path> walk = Files.walk(dir)) {

            indexHtmlFilesPaths = walk.map(Path::toString)
                    .filter(f -> f.contains(fileName))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexHtmlFilesPaths;
    }
}
