package ast.code.parser.javacodeparser;


import ast.code.parser.javacodeparser.service.*;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.HashSet;
import java.util.List;
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

        Set<String> views = new HashSet<>();

        String projectPath = "/home/tarfa/Phd/carl-mob-app";
        String layoutPathDestination = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/res/layout";
        Set<String> listClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        ));

        // todo Don't forget the Kotlin code
        // todo Don't forget the DTO Module we need to find a solution to get the DTO

        ProjectParser projectParser = new ProjectParser();
        PathResolver pathResolver = new PathResolver(projectParser);
        // first step: find the Paths of All dependencies
        Set<String> paths = pathResolver.findPaths(projectPath, listClasses, 10);

        // find other views
        paths.forEach(file -> {
            String content = null;
            try {
                content = FileHandler.read(file);
                CompilationUnit result = ParserFactory.getInstance(content);
                result.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(TypeDeclaration node) {
                        Set<String> fragmentView = ViewResolver.findFragmentView(node);
                        views.addAll(fragmentView);
                        return super.visit(node);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // find the path of view now
        List<String> fragmentViewPaths = pathResolver.getPaths(projectPath, views).stream()
                .filter(s -> !s.contains("build"))
                .collect(Collectors.toList());
        projectParser.copyToLayout(new HashSet<>(fragmentViewPaths), layoutPathDestination);
        fragmentViewPaths.forEach(System.out::println);

/*
        // second step: Create the packages
        String source = "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch";
        String target = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/";
        pathResolver.generatePackages(source, target);

        // third step: copy the files to new packages
        projectParser.copyTo(paths, "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java");

        Information parse = projectParser.parse(listClasses);
        // find fragment views
        List<String> fragmentViewPaths = pathResolver.getPaths(projectPath, parse.getFragmentView());
        projectParser.copyTo(new HashSet<>(fragmentViewPaths), layoutPathDestination);

        // find Activities views
        List<String> activityViewPaths = pathResolver.getPaths(projectPath, parse.getActivityViews());
        projectParser.copyTo(new HashSet<>(activityViewPaths), layoutPathDestination);
*/

    }

}
