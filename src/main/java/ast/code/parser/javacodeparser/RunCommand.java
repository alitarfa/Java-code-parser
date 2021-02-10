package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.FileHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class RunCommand {
    private Set<String> deps = new HashSet<>();

    public static void main(String[] args) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        RunCommand runCommand = new RunCommand();
        String projectPath = "/home/tarfa/Carl-work/Carl-touch-App/android/src/main/java/com/carl/touch/";
        String compiledProjectPath = "/home/tarfa/Carl-work/Carl-touch-App/android/build/intermediates/javac/acceptanceRelease/compileAcceptanceReleaseJavaWithJavac/classes/com/carl/touch/";
        Set<String> compiledClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Carl-work/Carl-touch-App/android/build/intermediates/javac/acceptanceRelease/compileAcceptanceReleaseJavaWithJavac/classes/com/carl/touch/android/activity/InitializationActivity.class",
                "/home/tarfa/Carl-work/Carl-touch-App/android/build/intermediates/javac/acceptanceRelease/compileAcceptanceReleaseJavaWithJavac/classes/com/carl/touch/android/activity/UpdateAppActivity.class",
                "/home/tarfa/Carl-work/Carl-touch-App/android/build/intermediates/javac/acceptanceRelease/compileAcceptanceReleaseJavaWithJavac/classes/com/carl/touch/android/activity/VisuEntityDetailActivity.class",
                "/home/tarfa/Carl-work/Carl-touch-App/android/build/intermediates/javac/acceptanceRelease/compileAcceptanceReleaseJavaWithJavac/classes/com/carl/touch/android/activity/DetailFragmentPagerAdapter.class"
        ));
        Set<String> paths = runCommand.findPaths(projectPath, compiledProjectPath, compiledClasses, 4, runtime);
        paths.forEach(System.out::println);

        // now i have the




    }


    public Set<String> findPaths(String projectPath, String compiledProjectPath, Set<String> compiledClasses, int depth, Runtime runtime) {
        for (int i = 0; i < depth; i++) {
            compiledClasses.forEach(classPath -> {
                try {
                    Set<String> importsByJDEPS = findImportsByJDEPS(runtime, classPath);
                    deps.addAll(importsByJDEPS);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            compiledClasses.addAll(getJavaPath(projectPath, compiledProjectPath, deps));
        }
        List<String> javaPath = getJavaPath(projectPath, compiledProjectPath, deps);
        return new HashSet<>(javaPath);
    }

    /**
     * Method takes List return by JDEPS result and return the list of dependencies
     *
     * @param input List of String
     * @return Set of dependencies
     */
    public Set<String> findClassNameDependencies(Set<String> input) {
        Set<String> collect = input.stream()
                .filter(s -> s.contains("com.carl.touch"))
                .filter(s -> !s.contains("java.lang"))
                .filter(s -> !s.contains("android.os"))
                .filter(s -> !s.contains("android.app"))
                .filter(s -> !s.contains("android.content.Context"))
                .filter(s -> !s.contains("android.content.Intent"))
                .map(s -> s.split("->"))
                .map(strings -> strings[1])
                .map(s -> s.split("\\."))
                .map(strings -> strings[strings.length - 1])
                .map(s -> s.split(" "))
                .map(strings -> strings[0])
                .collect(Collectors.toSet());

        return collect;
    }

    public Set<String> findImportsByJDEPS(Runtime runtime, String clazz) throws IOException, InterruptedException {
        Process p = runtime.exec("jdeps -v " + clazz);
        Set<String> list = new HashSet<>();
        new Thread(new Runnable() {
            public void run() {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                try {
                    while ((line = input.readLine()) != null) {
                        list.add(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        p.waitFor();
        return findClassNameDependencies(list);
    }

    public List<String> getJavaPath(String projectPath, String compiledProjectPath, Set<String> classes) {
        List<File> projectFiles = FileHandler.readCompiledJavaFiles(new File(compiledProjectPath));
        List<String> collect = classes.stream()
                .filter(s -> !s.contains("$"))
                //.peek(System.out::println)
                .map(s -> getPath(projectFiles, s))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                //.peek(System.out::println)
                .collect(Collectors.toList());
        return collect;
    }

    private Set<String> getPath(List<File> projectFiles, String target) {
        Set<String> found = new HashSet<>();
        // System.out.println(target);
        projectFiles.stream()
                .filter(file -> !file.getName().contains("$"))
                //.peek(System.out::println)
                .forEach(file -> {
                    String name = file.getName();
                    if (name.equalsIgnoreCase(target + ".class")) {
                        found.add(file.getAbsolutePath());
                    }
                });
        return found;
    }
}
