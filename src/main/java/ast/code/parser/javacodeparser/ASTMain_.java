package ast.code.parser.javacodeparser;

import ast.code.parser.javacodeparser.service.*;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ASTMain_ {

    public static void main(String[] args) throws Exception {
        // get one activity
        // find the view
        // go to the import section and get all dependencies and exclude the activities
        // for each dependency find its dependencies with excluding the activities

        // TODO: 12/01/2021 Implement it
        String projectPath = "/home/tarfa/Phd/carl-mob-app/android/src/main/java";
        String projectPathViews = "/home/tarfa/Phd/carl-mob-app/android/src";
        String path = "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java";
        String content = FileHandler.read(path);
        CompilationUnit result = ParserFactory.getInstance(content);
        ClassVisitors classVisitor = new ClassVisitors();
        result.accept(classVisitor);

        List<Set<String>> collect = classVisitor.getClasses()
                .stream()
                .map(ViewResolver::findActivityView)
                .filter(strings -> strings.size() > 0)
                .collect(Collectors.toList());
        // to print the view

        // now i need to get the dependencies with filtering the activities
        ProjectParser projectParser = new ProjectParser();
        Set<String> strings = projectParser.parse_v2(path)
                .stream()
                .filter(s -> !s.contains("Activity"))
                //.peek(System.out::println)
                .collect(Collectors.toSet());

        // find the dependencies
        PathResolver pathResolver = new PathResolver(projectParser);
        Set<String> paths = new HashSet<>(pathResolver.getJavaPath(projectPath, strings));


        // the found deps i need to find their deps
        Set<String> finalDeps = pathResolver.findPaths(projectPath, paths, 10, true);

        // find the fragments of the first cluster
        Set<String> fragment = finalDeps
                .stream()
                .map(s -> s.split("/"))
                .map(strings1 -> strings1[strings1.length - 1])
                .filter(s -> s.contains("Fragment"))
                .map(s -> s.split("\\."))
                .map(strings1 -> strings1[0])
                .collect(Collectors.toSet());

        // find the path of each fragment
        Set<String> fragmentsPaths = new HashSet<String>(pathResolver.getJavaPath(projectPath, fragment));

        // find the view of fragments
        Set<String> viewFragmentNames = new HashSet<>();
        fragmentsPaths.forEach(s -> {
            try {
                String contentFragments = FileHandler.read(s);
                CompilationUnit resultF = ParserFactory.getInstance(contentFragments);
                ClassVisitors classVisitorFr = new ClassVisitors();
                resultF.accept(classVisitorFr);
                classVisitorFr.getClasses().forEach(typeDeclaration -> {
                    Set<String> fragmentView = ViewResolver.findFragmentView(typeDeclaration);
                    viewFragmentNames.addAll(fragmentView);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // find path views
        List<String> viewFragmentPaths = pathResolver.getPaths(projectPathViews, viewFragmentNames);
        viewFragmentPaths.forEach(System.out::println);

        // construction
        String source = "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch";
        String target = "/home/tarfa/AndroidStudioProjects/MApp1/app/src/main/java/";
        pathResolver.generatePackages(source, target);
        projectParser.copyTo(finalDeps, target);

    }
}
