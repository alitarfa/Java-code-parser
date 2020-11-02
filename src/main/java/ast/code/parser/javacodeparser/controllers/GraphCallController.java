package ast.code.parser.javacodeparser.controllers;

import ast.code.parser.javacodeparser.service.CallingGraphService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GraphCallController {

    private final CallingGraphService callingGraphService;

    @PostMapping("/call-graph")
    public ResponseEntity<Set<Map.Entry<String, String>>> generateCallGraph(
            @RequestBody String projectPath
    ) throws Exception {
        Set<Map.Entry<String, String>> entries = this.callingGraphService.generateGraph(projectPath);
        return ResponseEntity.status(HttpStatus.OK).body(entries);
    }
}
