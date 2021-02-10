package ast.code.parser.javacodeparser.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathResolver {

    private final ProjectParser projectParser;
    private final Set<String> deps;

    public PathResolver(ProjectParser projectParser) {
        this.projectParser = projectParser;
        this.deps = new HashSet<>();
    }

    private Set<String> getPath(List<File> projectFiles, String target) {
        Set<String> found = new HashSet<>();
        projectFiles.forEach(file -> {
            String name = file.getName();
            if (name.equalsIgnoreCase(target + ".java") || name.equalsIgnoreCase(target + ".tk")) {
                found.add(file.getAbsolutePath());
            }
        });
        return found;
    }

    private Set<String> getPathXMLfile(List<File> projectFiles, String target) {
        Set<String> found = new HashSet<>();
        projectFiles.forEach(file -> {
            String name = file.getName();
            System.out.println(name);
            if (name.equalsIgnoreCase(target + ".xml")) {
                found.add(file.getAbsolutePath());
            }
        });
        return found;
    }

    public List<String> getJavaPath(String projectPath, Set<String> classes) {
        List<File> projectFiles = FileHandler.readJavaFiles(new File(projectPath));
        return classes.stream()
                .map(s -> getPath(projectFiles, s))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Set<String> findPaths(String projectPath, Set<String> listClasses, int depth, boolean exclude) {
        for (int i = 0; i < depth; i++) {
            listClasses.forEach(s -> {
                try {
                    Set<String> strings;
                    if (exclude) {
                        // todo this is just an option
                        strings = projectParser.parse_v2(s)
                                .stream()
                                .filter(s1 -> !s1.contains("Activity"))
                                .collect(Collectors.toSet());
                    } else {
                        // todo this is just an option
                        strings = projectParser.parse_v2(s);
                    }
                    deps.addAll(strings);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            listClasses.addAll(getJavaPath(projectPath, deps));
        }
        List<String> javaPath = getJavaPath(projectPath, deps);
        return javaPath.stream()
                .filter(s -> !s.contains("build"))
                .collect(Collectors.toSet());
    }

    public List<String> getPaths(String projectPath, Set<String> fragmentViews) {
        List<File> projectFiles = FileHandler.readXmlFiles(new File(projectPath));
        return fragmentViews.stream()
                .map(s -> getPathXMLfile(projectFiles, s))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void generatePackages(String source, String target) {
        List<File> files = FileHandler.readJavaFiles(new File(source));
        files.forEach(file -> {
            try {
                Stream<String> lines = Files.lines(file.toPath());
                Set<String> packages = lines
                        .filter(s -> s.contains("package com.carl.touch"))
                        .map(s -> s.replace("package", ""))
                        .map(s -> s.replace(";", ""))
                        .map(s -> s.replace(".", "/"))
                        .collect(Collectors.toSet());
                packages.forEach(s -> {
                    File file1 = new File(target + s.trim());
                    if (!file1.exists()) {
                        boolean mkdirs = file1.mkdirs();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
