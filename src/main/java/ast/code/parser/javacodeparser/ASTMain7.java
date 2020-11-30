package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.FileHandler;
import org.eclipse.jdt.core.IPackageFragment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ASTMain7 {

    /**
     * This Class with create a packages for the new project depending on the old projects
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String filePath = "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch";
        List<File> files = FileHandler.readJavaFiles(new File(filePath));
        files
                .forEach(file -> {
                    try {
                        Stream<String> lines = Files.lines(file.toPath());
                        Set<String> packages = lines
                                .filter(s -> s.contains("package com.carl.touch"))
                                .map(s -> s.replace("package", ""))
                                .map(s -> s.replace(";", ""))
                                .map(s -> s.replace(".", "/"))
                                .collect(Collectors.toSet());
                        packages.forEach(s -> {
                            File file1 = new File("/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/" + s.trim());
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
