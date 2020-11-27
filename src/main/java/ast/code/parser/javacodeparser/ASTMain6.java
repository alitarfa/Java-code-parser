package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.FileHandler;
import ast.code.parser.javacodeparser.service.PathResolver;
import ast.code.parser.javacodeparser.service.ProjectParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ASTMain6 {

    public static void main(String[] args) throws Exception {

        Set<String> deps = new HashSet<>();
        Set<String> paths = new HashSet<>();

        Set<String> listClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        ));

        ProjectParser projectParser = new ProjectParser();
        for (int i = 0; i < 10; i++) {
            listClasses.forEach(s -> {
                try {
                    Set<String> strings = projectParser.parse_v2(s);
                    deps.addAll(strings);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            String pathProject = "/home/tarfa/Phd/carl-mob-app/android/src/main/java";
            List<File> projectFiles = FileHandler.readJavaFiles(new File(pathProject));

            Set<String> collect = deps.stream()
                    .map(s -> PathResolver.getPath(projectFiles, s))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            listClasses.addAll(collect);
        }
        listClasses.forEach(System.out::println);

        // todo The Construction of the application
        // 1- copy the depends to the Android project
        // 2- find the views of Activities and Fragments
        // 3- copy the the views to the Android project
        // 4- copy the other resources to from the old project to the new One


        listClasses.forEach(s -> {
            try {
                projectParser.copyTo(s, "/home/tarfa/MySpace/TestCopy");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
