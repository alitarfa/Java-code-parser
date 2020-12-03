package ast.code.parser.javacodeparser.controllers;

import ast.code.parser.javacodeparser.models.Information;
import ast.code.parser.javacodeparser.service.*;
import lombok.AllArgsConstructor;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parser-project")
@AllArgsConstructor
public class ParseController {

    private SimpMessageSendingOperations messagingTemplate;
    private ProjectParser projectParser;

    @GetMapping
    public void parseProject() {
        Map<String, Boolean> stringMap = new HashMap<>();
        stringMap.put("init", true);
        messagingTemplate.convertAndSend("/parsing-steps", stringMap);

        Set<String> views = new HashSet<>();
        String projectPath = "/home/tarfa/Phd/carl-mob-app";
        String layoutPathDestination = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/res/layout";
        Set<String> listClasses = new java.util.HashSet<>(Set.of(
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/InitializationActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/UpdateAppActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/activity/VisuEntityDetailActivity.java",
                "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch/android/utils/adapter/DetailFragmentPagerAdapter.java"
        ));

        PathResolver pathResolver = new PathResolver(projectParser);
        Set<String> paths = pathResolver.findPaths(projectPath, listClasses, 10);
        messagingTemplate.convertAndSend("/topic", "Generating Packages");
        String source = "/home/tarfa/Phd/carl-mob-app/android/src/main/java/com/carl/touch";
        String target = "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java/";
        pathResolver.generatePackages(source, target);
        projectParser.copyTo(paths, "/home/tarfa/AndroidStudioProjects/MicroAppClusterA/app/src/main/java");
        Information parse = projectParser.parse(listClasses);
        stringMap.put("dep", true);
        messagingTemplate.convertAndSend("/parsing-steps", stringMap);


        List<String> activityViewPaths = pathResolver.getPaths(projectPath, parse.getActivityViews());
        projectParser.copyTo(new HashSet<>(activityViewPaths), layoutPathDestination);
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
        stringMap.put("view", true);
        messagingTemplate.convertAndSend("/parsing-steps", stringMap);

        // TODO: 03/12/2020 add the packages fix
        stringMap.put("pack", true);
        messagingTemplate.convertAndSend("/parsing-steps", stringMap);

        // TODO: 03/12/2020 fish
        stringMap.put("fin", true);
        messagingTemplate.convertAndSend("/parsing-steps", stringMap);

    }
}
