package ast.code.parser.javacodeparser.controllers;

import ast.code.parser.javacodeparser.models.DependencyModel;
import ast.code.parser.javacodeparser.service.CallingGraphService;
import ast.code.parser.javacodeparser.service.DependencyGraphService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GraphController {

    private final CallingGraphService callingGraphService;
    private final DependencyGraphService dependencyGraphService;

    @PostMapping("/call-graph")
    public ResponseEntity<Set<Map.Entry<String, String>>> generateCallGraph(
            @RequestBody String projectPath
    ) throws Exception {
        Set<Map.Entry<String, String>> entries = this.callingGraphService.generateGraph(projectPath);
        return ResponseEntity.status(HttpStatus.OK).body(entries);
    }

    @PostMapping("/dependency-graph")
    public ResponseEntity<Set<DependencyModel>> generateDependencyGraph(@RequestBody String projectPath) {
        Set<DependencyModel> dependencyModels = dependencyGraphService.generateDependencyGraph(projectPath);
        return ResponseEntity.status(HttpStatus.OK).body(dependencyModels);
    }
}
