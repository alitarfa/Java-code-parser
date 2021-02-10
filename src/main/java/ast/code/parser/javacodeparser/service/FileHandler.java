package ast.code.parser.javacodeparser.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileHandler {

    public static String read(String filePath) throws Exception {
        Path path = Paths.get(filePath);
        try {
            return Files.readString(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new Exception("error.file.read.failed");
        }
    }

    public static List<File> readJavaFiles(File folder) {
        List<File> javaFiles = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                javaFiles.addAll(readJavaFiles(file));
            } else if (file.getName().endsWith(".java") || file.getName().endsWith(".kt")) {
                javaFiles.add(file);
            }
        }
        return javaFiles;
    }

    public static List<File> readCompiledJavaFiles(File folder) {
        List<File> javaFiles = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                javaFiles.addAll(readCompiledJavaFiles(file));
            } else if (file.getName().endsWith(".class")) {
                javaFiles.add(file);
            }
        }
        return javaFiles;
    }

    public static List<File> readXmlFiles(File folder) {
        List<File> xmlFile = new ArrayList<>();
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isDirectory()) {
                xmlFile.addAll(readXmlFiles(file));
            } else if (file.getName().endsWith(".xml")) {
                xmlFile.add(file);
            }
        }
        return xmlFile;
    }
}
