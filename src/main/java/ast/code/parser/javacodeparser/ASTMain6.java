package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.models.Information;
import ast.code.parser.javacodeparser.service.*;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ASTMain6 {

    /**
     * This Class helps to get the all dependencies of given cluster
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Set<String> deps = new HashSet<>();
        String projectPath = "/home/tarfa/Phd/carl-mob-app";
        String layoutPathDestination = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/res/layout";
        String srcJavaPathDestination = "";
        List<File> projectFiles = FileHandler.readJavaFiles(new File(projectPath));
        Set<String> listClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        ));

        // todo Don't forget the Kotlin code
        // todo Don't forget the DTO Module we need to find a solution to get the DTO

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

            Set<String> pathClasses = deps.stream()
                    .map(s -> PathResolver.getPath(projectFiles, s))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            listClasses.addAll(pathClasses);
        }

        Information parse = projectParser.parse(listClasses);
        List<String> fragmentViewPaths = PathResolver.getPaths(projectPath, parse.getFragmentView());
        List<String> activityViewPaths = PathResolver.getPaths(projectPath, parse.getActivityViews());

        // copy the
        projectParser.copyTo(activityViewPaths, layoutPathDestination);

        // TODO: 27/11/2020 in the from work on steps to present all stpes
        // reading files parsing finding dependes and finding path and .....

    }

}
