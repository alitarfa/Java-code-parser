package ast.code.parser.javacodeparser.controllers;

import ast.code.parser.javacodeparser.service.*;
import ast.code.parser.javacodeparser.typevisitors.ClassVisitors;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AlgoClustering {


    @GetMapping("/api/find-deps-activity")
    public ResponseEntity<Set<String>> findDepsOfOneActivity() throws Exception {
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

        // now i need to get the dependencies with filtering the activities
        ProjectParser projectParser = new ProjectParser();
        Set<String> strings = projectParser.parse_v2(path)
                .stream()
                .filter(s -> !s.contains("Activity"))
                .peek(System.out::println)
                .collect(Collectors.toSet());

        // find the dependencies
        PathResolver pathResolver = new PathResolver(projectParser);
        Set<String> paths = new HashSet<>(pathResolver.getJavaPath(projectPath, strings));
        paths.forEach(System.out::println);
        // the found deps i need to find their deps
        Set<String> finalDeps = pathResolver.findPaths(projectPath, paths, 10, true);
        System.out.println(finalDeps.size());
        return ResponseEntity.ok(finalDeps);
    }
}
