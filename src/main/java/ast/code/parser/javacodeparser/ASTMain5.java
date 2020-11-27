package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.models.Information;
import ast.code.parser.javacodeparser.service.FileHandler;
import ast.code.parser.javacodeparser.service.PathResolver;
import ast.code.parser.javacodeparser.service.ProjectParser;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ASTMain5 {

    public static void main(String[] args) throws Exception {
        Set<String> listClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        ));

        for (int i = 0; i < 20; i++) {
            ProjectParser projectParser = new ProjectParser();
            Information parse = projectParser.parse(listClasses);
            parse.getDependencies().forEach(System.out::println);
            System.out.println("****************** views *********************");
            parse.getView().forEach(System.out::println);
            // get path of each depends
            Set<String> collect = parse.getDependencies()
                    .stream()
                    .map(s -> {
                        String pathProject = "/home/tarfa/Phd/carl-mob-app/android/src/main/java";
                        List<File> projectFiles = FileHandler.readJavaFiles(new File(pathProject));
                        return PathResolver.getPath(projectFiles, s);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            listClasses.addAll(collect);
        }
    }
}
