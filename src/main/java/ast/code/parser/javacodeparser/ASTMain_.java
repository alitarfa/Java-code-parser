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
        collect.forEach(System.out::println);

        // now i need to get the dependencies with filtering the activities
        ProjectParser projectParser = new ProjectParser();
        Set<String> strings = projectParser.parse_v2(path)
                .stream()
                .filter(s -> !s.contains("Activity"))
                //.peek(System.out::println)
                .collect(Collectors.toSet());

        // find the dependencies
        strings.forEach(System.out::println);
        System.out.println("**********************************");
        PathResolver pathResolver = new PathResolver(projectParser);
        Set<String> paths = new HashSet<>(pathResolver.getJavaPath(projectPath, strings));
        paths.forEach(System.out::println);
    }

}
